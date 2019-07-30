import com.example.demo.main.Client;

public class SocketClient {

    public static void main(String[] args) {
        try {
            String str = "reqLoginAlarm;user=iottest;key=iottest;type=msg";
            // String str="reqSyncAlarmMsg;reqId=33;alarmSeq=10";
            Client client = new Client();
            client.start(str);
            System.out.println("本次通话结束。。。。");
        } catch (Exception e) {
            System.out.println("客户端运行失败");
        }


    }
}
