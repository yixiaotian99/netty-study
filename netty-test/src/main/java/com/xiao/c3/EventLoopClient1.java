package com.xiao.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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
public class EventLoopClient1 {

    public static void main(String[] args) throws InterruptedException {

        //定义 Future 对象
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    //在连接建立后调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //1.连接到服务器
                //异步非阻塞线程，异步指main线上发起调用，真正执行connect的是nio线程
                .connect(new InetSocketAddress("localhost", 8081));


        //2.必须要进行 sync 阻塞，否则程序会继续向下执行，还没等连接建立就去 channelFuture.channel(); 获取不到channel
//        channelFuture.sync();

        Channel channel = channelFuture.channel();

        //获取到 channel 使用 idea debug 模式, 调用 channel.writeAndFlush("aaa")发送数据
        log.info("获取到channel:{}", channel); //获取到channel:[id: 0x93d0a50b, L:/127.0.0.1:52833 - R:localhost/127.0.0.1:8081] 假如没有  channelFuture.sync(); 拿到空channel


    }
}
