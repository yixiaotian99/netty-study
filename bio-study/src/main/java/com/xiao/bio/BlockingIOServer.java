package com.xiao.bio;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author sunjinwei
 * @Date 2020-07-03 09:20
 * @Description 模拟 Blocking IO 阻塞服务器，客户端使用 telnet 模拟
 **/
public class BlockingIOServer {


    public static void main(String[] args) throws IOException {
        serverListen();
    }


    /**
     * 服务端一直阻塞监听
     *
     * @throws IOException 目标：客户端使用 telnet 连接上来，输入"hello,server"
     *                     服务端回复"hi,ip"
     */
    private static void serverListen() throws IOException {

        //思路
        //1. 创建一个线程池，来一个客户端就开启一个线程
        //2. 使用 ServerSocket


        //服务器监听
        ServerSocket serverSocket = new ServerSocket(6666);

        //一直循环保证监听
        while (true) {

            //循环打印
            System.out.println("服务端阻塞等待");


            //阻塞方法1，一直等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("有客户端连接了");

            //开启新线程处理客户端请求
            handleClient(socket);
        }
    }

    private static void handleClient(Socket socket) {

        //线程池
        Executor executor = Executors.newCachedThreadPool();

        //开启线程
        executor.execute(() -> {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                //阻塞方法2，等待客户端输入
                inputStream = socket.getInputStream();

                byte[] bytes = new byte[1024];
                while (true){
                    int read = inputStream.read(bytes); //将数据读取到字节数组

                    if(read!=-1){
                        String s = new String(bytes, 0, read);
                        System.out.println("客户端输入: " + s);


                        //获取客户端网络地址 ip
                        InetAddress inetAddress = socket.getInetAddress();

                        //写回客户
                        outputStream = socket.getOutputStream();
                        String response = "你好，ip=" + inetAddress.getHostAddress() + "\\r\\n";
                        outputStream.write(response.getBytes());

                        outputStream.flush();
                    }
                }

//                //只会一个字符一个字符输出
//                while (inputStream.available() != -1) {
//                    System.out.printl"客户端输入: " + (char) inputStream.read());
//                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

    }

}
