import com.example.demo.main.Client;
import com.example.demo.main.TestHead;

import java.io.*;
import java.net.Socket;

public class SocketClient {

    public static void main(String[] args) {
      /* try {
            String str1 = "reqLoginAlarm;user=iottest;key=iottest;type=msg";
            String str="closeConnAlarm";
            Client client = new Client();
            client.start(str1);
            client.start(str);
            System.out.println("本次通话结束。。。。");
        } catch (Exception e) {
            System.out.println("客户端运行失败");
        }*/

        try {
            Socket s = new Socket("192.168.199.138", 31232);
            System.out.println("客户端IP:"+s.getLocalAddress()+"端口"+s.getPort());
            //构建IO流
            OutputStream opt=s.getOutputStream();

            /**
             *多个output设置循环输出
             */
            String str1 = "reqLoginAlarm;user=iottest;key=iottest;type=msg";
            byte msgType = 1;
            byte[]allmessage=TestHead .creatMessage(str1,msgType);
            BufferedOutputStream bos = new BufferedOutputStream(opt);
            bos.write(allmessage);
            bos.flush();

//            Thread.sleep(200);
//            String str2 = "reqHeartBeat;reqId=33";
//            byte msgType1 = 8;
//            byte[]allmessage1=TestHead .creatMessage(str2,msgType1);
//            bos.write(allmessage1);
//            bos.flush();

            /**
             * 多个input测试循环接收
             */
            InputStream is=s.getInputStream();
            byte b[]=new byte[1024*1024];
            is.read(b);
            System.out.println("相应"+TestHead.resolveMessage(b));

            /*InputStream is1=s.getInputStream();
            byte b1[]=new byte[1024*1024];
            is1.read(b1);
            System.out.println("相应"+TestHead.resolveMessage(b1));

            InputStream is2=s.getInputStream();
            byte b2[]=new byte[1024*1024];
            is1.read(b2);
            System.out.println("相应"+TestHead.resolveMessage(b2));*/







        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1)
            throw new IllegalArgumentException("this byteArray must not be null or empty");

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 0xff) < 0x10)//0~F前面不零
                hexString.append("0");
            hexString.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return hexString.toString().toLowerCase();
    }




}
