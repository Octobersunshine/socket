import com.example.demo.main.Client;
import com.example.demo.main.TestHead;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

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
            Socket s = new Socket("192.168.43.242", 31232);
            System.out.println("客户端IP:"+s.getLocalAddress()+"端口"+s.getPort());
            //构建IO流
            OutputStream opt=s.getOutputStream();

            //建立键盘输入：d
                Scanner scanner = new Scanner(System.in);
                System.out.println("请输入发送消息内容：");
                String str1 = "reqLoginAlarm;user=iottest;key=iottest;type=msg";
                 byte msgType = 1;
                byte[]allmessage=TestHead .creatMessage(str1,msgType);
                 BufferedOutputStream bos = new BufferedOutputStream(opt);
                bos.write(allmessage);
                bos.flush();

                InputStream in = s.getInputStream();
                byte[] bytes = {};
                Thread.sleep(50);
                int bufflenth = in.available();
                while (bufflenth != 0)
                {
                bytes = new byte[bufflenth]; // 初始化byte数组为buffer中数据的长度
                in.read(bytes);
                bufflenth = in.available();
                }
               System.out.println("相应"+TestHead.resolveMessage(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
