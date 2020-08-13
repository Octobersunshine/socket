package com.example.demo.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * socket
 */
public class Client {
    /**
     * java.net.Socket;
     * 封装tcp通讯协议.使用它可以基于tcp与远程计算机
     * 上的服务端应用程序链接并进行通讯
     */
    private Socket socket;
    public OutputStream outputStream ;
    public InputStream inputStream ;


    /**
     * 初始化客户端
     */
    public Client() throws Exception {
        try {
            /**
             * 实例化socket就是与服务器端建立链接
             * 的过程这里需要传入两个参数来指定服务端的信息
             * 参数1：服务端计算机的ip
             * 参数2：运行在服务端计算机上的服务端应用程序打开的
             * 服务端口
             * 通过ip可以找到服务端计算机   在通过端口可以链接到
             * 运行在服务端计算机上的服务端应用程序
             * 由于实例化就是链接的过程若服务端没有响应这里的socket会抛出异常
             */
            System.out.println("正在建立链接！");
            socket = new Socket("192.168.43.242", 31232);
          outputStream = socket.getOutputStream();
           inputStream = socket.getInputStream();
            System.out.println("与服务端建立链接");
        } catch (Exception e) {
            System.out.println("链接失败");
            //将来针对异常可能要记录日志

        }
    }

    /**
     * 客户端开始工作
     */
    public void start(String str) {
        try {
            System.out.println("3333");
            PrintWriter pw = new PrintWriter(outputStream);
            pw.println(str);
            pw.flush();


                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String result=null;
                while((result=br.readLine())!=null){
                    System.out.println(result);
                }


            inputStream.close();
            outputStream.close();


        } catch (Exception e) {
        }

    }



}
