package com.xiao.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author sunjinwei
 * @Date 2020-07-03 16:50
 * @Description 使用 nio 客户端
 **/
public class NioClient {


    public static void main(String[] args) throws IOException {
        //创建 SocketChannel
        SocketChannel socketChannel = SocketChannel.open();

        //配置非阻塞模式
        socketChannel.configureBlocking(false);

        //配置服务器监听
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 6666);

        //判断是否服务器正常
        if (!socketChannel.connect(socketAddress)) {

            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他事情");
            }
        }


        //定义缓冲区
        ByteBuffer bytebuffer = ByteBuffer.allocate(20);
        bytebuffer.put("你好，服务端".getBytes());

        //写入缓冲区
        socketChannel.write(bytebuffer);

        //防止程序退出
        //System.in.read();
    }
}
