package com.example.demo.main;

import com.example.demo.util.StringUtil;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class TalkServer implements Runnable {
    Socket socket;
    InputStream in = null;
    OutputStream out = null;

    public TalkServer(Socket socket) {
        this.socket = socket;
    }

    //业务逻辑：跟客户端进行数据交互
    @Override
    public void run() {
        try {
            //succ登录状态
            boolean succ = false;
            //从socket连接中获取到与client之间的网络通信输入输出流
            in = socket.getInputStream();
            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //从网络通信输入流中读取客户端发送过来的数据
            //注意：socketinputstream的读数据的方法都是阻塞的
            String resolveParam = br.readLine();
            //解析resolveParam
            Map<String, String> resolveMap = resolveParam(resolveParam);

            if (succ) {
                //发现缺失告警时
                String reqSyncAlarmMsg = resolveMap.get("reqSyncAlarmMsg");
                if ("reqSyncAlarmMsg".equals(reqSyncAlarmMsg)) {
                    //alarmSeq丢失多条告警时，为最小告警消息序列号 对应数据库中告警id
                    String alarmSeq = resolveMap.get("alarmSeq");
                    //根据alarmSeq查找数据
                    String res = "json数据";
                    if (StringUtil.isNotNull(res)) {
                        returnMessage(out, res);

                    } else {
                        String reqId = resolveMap.get("reqId");
                        String returnMessage = "ackSyncAlarmMsg;reqId=" + reqId + ";result=fail;resDesc=null";
                        returnMessage(out, returnMessage);
                    }

                }
                String reqHeartBeat = resolveMap.get("reqHeartBeat");
                if ("reqHeartBeat".equals(reqHeartBeat)) {
                    String message = "ackHeartBeat;reqId=33";
                    returnMessage(out, message);
                }
                String closeConnAlarm = resolveMap.get("closeConnAlarm");
                if ("closeConnAlarm".equals(closeConnAlarm)) {
                    returnMessage(out, "closeConnAlarm");
                    out.close();
                    socket.close();
                }

            } else {
                String messageName = resolveMap.get("reqLoginAlarm");
                if (StringUtil.isNotNull(messageName)) {
                    if ("reqLoginAlarm".equals(messageName)) {
                        //reqLoginAlarm登录请求
                        String user = resolveMap.get("user");
                        String key = resolveMap.get("key");
                        String puser = GetValueByKey("user");
                        String pkey = GetValueByKey("key");
                        if (puser.equals(user) && pkey.equals(key)) {
                            //验证成功上报数据json格式
                            String returnMessage = "json数据。。。。。。";
                            returnMessage(out, returnMessage);
                            succ = true;
                        } else {
                            //验证失败
                            String returnMessage = "ackLoginAlarm;result=fail;resDesc=usernameOrpass-error";
                            returnMessage(out, returnMessage);
                        }
                    }

                } else {
                    String returnMessage = "ackLoginAlarm;result=fail;resDesc=消息名不能为空";
                    returnMessage(out, returnMessage);

                }

            }

        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void returnMessage(OutputStream out, String returnMessage) {
        PrintWriter pw = new PrintWriter(out);
        pw.println(returnMessage);
        pw.flush();
    }


    /**
     * 解析消息体样例：reqLoginAlarm;user=yiy;key=qw#$@;type=msg
     *
     * @param resolveParam
     * @return
     */
    public static Map<String, String> resolveParam(String resolveParam) {
        Map<String, String> strmap = new HashMap<String, String>();
        int first1 = resolveParam.indexOf(";");
        String messageName = resolveParam.substring(0, first1);
        System.out.println("消息名" + messageName);
        String paramGroup = resolveParam.substring(first1 + 1);
        System.out.println("参数组" + paramGroup);
        strmap.put(messageName, messageName);
        String[] params = paramGroup.split(";");
        for (int i = 0; i < params.length; i++) {
            System.out.println(params[i] + "0");
            String[] param = params[i].split("=");
            strmap.put(param[0], param[1]);
        }
        System.out.println(strmap.toString());
        System.out.println(strmap.get("user"));
        return strmap;
    }

    /**
     * 读取NMS用户信息 进行校验
     *
     * @param key
     * @return
     */
    public static String GetValueByKey(String key) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream("src/main/resources/nms_account.properties"));
            pps.load(in);
            String value = pps.getProperty(key);
            return value;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(8088);
            // 接受客户端的连接请求;accept是一个阻塞方法，会一直等待，到有客户端请求连接才返回
            while (true) {
                System.out.println("等待连接.....");
                Socket socket = server.accept();
                System.out.println("连接成功....");
                new Thread(new TalkServer(socket)).start();
            }
        } catch (Exception e) {

        }


    }

}
