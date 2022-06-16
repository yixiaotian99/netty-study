package com.xiao.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @className: HelloClient
 * @description: TODO 类描述
 * @author: sunjinwei
 * @date: 2022/6/16 00:02
 **/
@Slf4j
public class EventLoopClient {

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
                .connect(new InetSocketAddress("localhost", 8081))
                .sync()
                .channel();

        //获取到 channel 使用 idea debug 模式, 调用 channel.writeAndFlush("aaa")发送数据
        log.info("获取到channel:{}", channel); //在这一行打断点，注意右键断点---选择Thread模式，否则会把所有线程都停下来


    }
}
