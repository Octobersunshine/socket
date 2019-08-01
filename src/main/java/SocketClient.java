import com.example.demo.main.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient {

    public static void main(String[] args) {
     /*   try {
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
            Socket s = new Socket("192.168.1.101", 31232);
            System.out.println("客户端IP:"+s.getLocalAddress()+"端口"+s.getPort());
            //构建IO流
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            //建立键盘输入：
            Scanner scanner = new Scanner(System.in);
            while(true){
                System.out.println("请输入发送消息内容：");
                bw.write(scanner.nextLine()+"\n");
                bw.newLine();
                bw.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                //读取服务器返回的消息数据
                System.out.println(s.getInetAddress().getLocalHost()+":"+s.getPort()+">>"+br.readLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
