package com.example.demo.socket;

import com.example.demo.util.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerResponseThread implements Runnable {
    private ReceiveThread receiveThread;
    private SendThread sendThread;
    private Socket socket;
    private SocketServerResponseInterface socketServerResponseInterface;
    private volatile ConcurrentLinkedQueue<String> dataQueue = new ConcurrentLinkedQueue<>();
    private static ConcurrentHashMap<String, Socket> onLineClient = new ConcurrentHashMap<>();
    private long lastReceiveTime = System.currentTimeMillis();
    private String userIP;
    public String getUserIP() {
        return userIP;
    }

    public ServerResponseThread(Socket socket, SocketServerResponseInterface socketServerResponseInterface) {
        this.socket = socket;
        this.socketServerResponseInterface = socketServerResponseInterface;
        this.userIP = socket.getInetAddress().getHostAddress();
        onLineClient.put(userIP, socket);
        System.out.println("客户端IP：" + userIP);
    }

    @Override
    public void run() {
        try {
            //开启接收线程
            receiveThread = new ReceiveThread();
            receiveThread.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            receiveThread.start();
            //开启发送线程
            sendThread = new SendThread();
            sendThread.printWriter = new PrintWriter(socket.getOutputStream(), true);
            sendThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开socket连接
     */
    public void stop() {
        try {
            System.out.println("stop");
            if (receiveThread != null) {
                receiveThread.isCancel = true;
                receiveThread.interrupt();
                if (receiveThread.bufferedReader != null) {
                    SocketUtil.inputStreamShutdown(socket);
                    System.out.println("before closeBufferedReader");
                    SocketUtil.closeBufferedReader(receiveThread.bufferedReader);
                    System.out.println("after closeBufferedReader");
                    receiveThread.bufferedReader = null;
                }
                receiveThread = null;
                System.out.println("stop receiveThread");
            }

            if (sendThread != null) {
                sendThread.isCancel = true;
                toNotifyAll(sendThread);
                sendThread.interrupt();
                if (sendThread.printWriter != null) {
                    //防止写数据时停止，写完再停
                    synchronized (sendThread.printWriter) {
                        SocketUtil.closePrintWriter(sendThread.printWriter);
                        sendThread.printWriter = null;
                    }
                }
                sendThread = null;
                System.out.println("stop sendThread");
            }
            /*onLineClient.remove(userIP);
            System.out.println("用户：" + userIP
                    + " 退出,当前在线人数:" + onLineClient.size());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     */
    public void addMessage(String data) {
        if (!isConnected()) {
            return;
        }

        dataQueue.offer(data);
        //有新增待发送数据，则唤醒发送线程
        toNotifyAll(dataQueue);
    }


    /**
     * 阻塞线程,millis为0则永久阻塞,知道调用notify()
     */
    public void toWaitAll(Object o) {
        synchronized (o) {
            try {
                o.wait();
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * notify()调用后，并不是马上就释放对象锁的，而是在相应的synchronized(){}语句块执行结束，自动释放锁后
     */
    public void toNotifyAll(Object obj) {
        synchronized (obj) {
            obj.notifyAll();
        }
    }

    /**
     * 判断本地socket连接状态
     */
    private boolean isConnected() {
        if (socket.isClosed() || !socket.isConnected()) {
            onLineClient.remove(userIP);
            ServerResponseThread.this.stop();
            System.out.println("socket closed...");
            return false;
        }
        return true;
    }

    /**
     * 数据接收线程
     */
    public class ReceiveThread extends Thread {
        private BufferedReader bufferedReader;
        private boolean isCancel;
        private boolean logoned = false;

        @Override
        public void run() {
            try {
                while (!isCancel) {
                    if (!isConnected()) {
                        isCancel = true;
                        break;
                    }
                    String msg = SocketUtil.readFromStream(bufferedReader);
                    if (msg != null) {
                        Map<String, String> resolveMap = SocketUtil.resolveParam(msg);
                        if (logoned) {
                            String reqSyncAlarmMsg = resolveMap.get("reqSyncAlarmMsg");
                            if ("reqSyncAlarmMsg".equals(reqSyncAlarmMsg)) {
                                //alarmSeq丢失多条告警时，为最小告警消息序列号 对应数据库中告警id
                                String alarmSeq = resolveMap.get("alarmSeq");
                                //根据alarmSeq查找数据
                                String res = "json数据";
                                if (StringUtil.isNotNull(res)) {
                                    addMessage(res);
                                    socketServerResponseInterface.clientOnline(userIP);
                                } else {
                                    String reqId = resolveMap.get("reqId");
                                    String returnMessage = "ackSyncAlarmMsg;reqId=" + reqId + ";result=fail;resDesc=null";
                                    addMessage(returnMessage);
                                    socketServerResponseInterface.clientOnline(userIP);
                                }

                            }
                            String reqHeartBeat = resolveMap.get("reqHeartBeat");
                            if ("reqHeartBeat".equals(reqHeartBeat)) {
                                String message = "ackHeartBeat;reqId=33";
                                addMessage(message);
                                socketServerResponseInterface.clientOnline(userIP);
                            }
                            String closeConnAlarm = resolveMap.get("closeConnAlarm");
                            if ("closeConnAlarm".equals(closeConnAlarm)) {
                                addMessage("closeConnAlarm");
                                socketServerResponseInterface.clientOnline(userIP);
                            }

                        }else{
                            String messageName = resolveMap.get("reqLoginAlarm");
                            if ("reqLoginAlarm".equals(messageName)) {
                                //reqLoginAlarm登录请求
                                String user = resolveMap.get("user");
                                String key = resolveMap.get("key");
                                String puser = SocketUtil.GetValueByKey("user");
                                String pkey = SocketUtil.GetValueByKey("key");
                                if (puser.equals(user) && pkey.equals(key)) {
                                    //验证成功上报数据json格式
                                    String returnMessage = "json数据。。。。。。";
                                    System.out.println(returnMessage);
                                    addMessage(returnMessage);
                                    socketServerResponseInterface.clientOnline(userIP);
                                    logoned = true;
                                } else {
                                    //验证失败
                                    String returnMessage = "ackLoginAlarm;result=fail;resDesc=usernameOrpass-error";
                                    addMessage(returnMessage);
                                    socketServerResponseInterface.clientOnline(userIP);
                                }
                            }
                        }

                    } else {
                        System.out.println("client is offline...");
                        ServerResponseThread.this.stop();
                        socketServerResponseInterface.clientOffline();
                        break;
                    }
                    System.out.println("ReceiveThread");
                }

                SocketUtil.inputStreamShutdown(socket);
                SocketUtil.closeBufferedReader(bufferedReader);
                System.out.println("ReceiveThread is finish");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 数据发送线程,当没有发送数据时让线程等待
     */
    public class SendThread extends Thread {

        private PrintWriter printWriter;
        private boolean isCancel;

        @Override
        public void run() {
            try {
                while (!isCancel) {
                    if (!isConnected()) {
                        isCancel = true;
                        break;
                    }
                    String msg = dataQueue.poll();
                    if (msg == null) {
                        toWaitAll(dataQueue);
                    } else if (printWriter != null) {
                        synchronized (printWriter) {
                            SocketUtil.write2Stream(msg, printWriter);
                        }
                    }
                    System.out.println("SendThread");
                }
                SocketUtil.outputStreamShutdown(socket);
                SocketUtil.closePrintWriter(printWriter);
                System.out.println("SendThread is finish");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
