package com.xiao.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author sunjinwei
 * @Date 2020-07-03 16:21
 * @Description 使用 ServcerSocketChannel 创建服务器
 **/
public class NioServer {


    public static void main(String[] args) throws IOException {
        //创建 ServerSocketChannel -> SocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //配置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //获取 selector
        Selector selector = Selector.open();

        //将 ServerSocketChannel 注册到 selector, 监听的事件是 accept 即客户端连接请求
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        //循环读取
        while (true) {

            //System.out.println("服务器已经准备好，等待客户端连接...");

            //使用 selector 监听所有事件，间隔2000毫秒
            if (selector.select(5000) == 0) {
                //System.out.println("服务器未监听到任何事件");
                continue;
            }

            //如果有事件发生,获取 selectionKey 集合
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            //循环得到的事件列表
            while (iterator.hasNext()) {

                //得到事件
                SelectionKey key = iterator.next();


                //如果是连接请求，新生成一个 socketChannel
                if (key.isAcceptable()) {

                    //为该客户端生成 socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端已经连接上了");

                    //配置非阻塞
                    socketChannel.configureBlocking(false);

                    //为该客户端生成读事件，并关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                //如果有读事件发生
                if (key.isReadable()) {

                    //通过 key 反向查找 channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();

                    //获取客户端一些基本信息
                    SocketAddress address = channel.getRemoteAddress();

                    //读取客户端发送
                    channel.read(byteBuffer);

                    System.out.println("客户端输入: " + new String(byteBuffer.array(), Charset.forName("utf-8")));
                }

                //手动移除 selectionKey 防止重复事件发生
                iterator.remove();

            }

        }
    }

}
