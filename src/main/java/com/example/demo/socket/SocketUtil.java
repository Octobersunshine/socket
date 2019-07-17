package com.example.demo.socket;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SocketUtil {
    public static String ADDRESS = "localhost";
    public static int PORT = 8088;

    /**
     * 读数据
     *
     * @param bufferedReader
     */
    public static String readFromStream(BufferedReader bufferedReader) {
        try {
            String s;
            if ((s = bufferedReader.readLine()) != null) {
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写数据
     *
     * @param data
     * @param printWriter
     */
    public static void write2Stream(String data, PrintWriter printWriter) {
        if (data == null) {
            return;
        }
        if (printWriter != null) {
            printWriter.println(data);
        }
    }


    /**
     * 关闭输入流
     *
     * @param socket
     */
    public static void inputStreamShutdown(Socket socket) {
        try {
            if (!socket.isClosed() && !socket.isInputShutdown()) {
                socket.shutdownInput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭BufferedReader
     *
     * @param br
     */
    public static void closeBufferedReader(BufferedReader br) {
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流
     *
     * @param socket
     */
    public static void outputStreamShutdown(Socket socket) {
        try {
            if (!socket.isClosed() && !socket.isOutputShutdown()) {
                socket.shutdownOutput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭PrintWriter
     *
     * @param pw
     */
    public static void closePrintWriter(PrintWriter pw) {
        if (pw != null) {
            pw.close();
        }
    }

    /**
     * 获取本机IP地址
     */
    public static String getIP() {
        String hostIP = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;
                        // skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIP = ia.getHostAddress();
                        break;
                    }
                }
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIP;
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

}
