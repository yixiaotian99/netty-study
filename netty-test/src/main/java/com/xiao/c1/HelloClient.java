package com.xiao.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @className: HelloClient
 * @description: TODO 类描述
 * @author: sunjinwei
 * @date: 2022/6/16 00:02
 **/
public class HelloClient {

    public static void main(String[] args) throws InterruptedException {
        //1.启动类
        Channel channel = new Bootstrap()
                //2.增加 EventLoop 组
                .group(new NioEventLoopGroup())
                //3.选择客户端 channel 实现
                .channel(NioSocketChannel.class)
                //4.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    //在连接建立后调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //5.连接到服务器
                .connect(new InetSocketAddress("localhost", 8088))
                .sync()
                .channel();

                //6.向服务器发送数据
        channel.writeAndFlush("hello world 你好世界");
        System.out.println("channelId:"+  channel.id());

    }
}
