package com.xiao.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @className: HelloClient
 * @description: TODO 类描述
 * @author: sunjinwei
 * @date: 2022/6/16 00:02
 **/
@Slf4j
public class EventLoopClient4 {

    public static void main(String[] args) throws InterruptedException {

        //定义 Future 对象
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {

                    //在连接建立后调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //增加日志处理类
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));

                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //1.连接到服务器
                //异步非阻塞线程，异步指main线上发起调用，真正执行connect的是nio线程
                .connect(new InetSocketAddress("localhost", 8081));

        //获取 channel
        Channel channel = channelFuture.sync().channel();

        //2. 接收用户控制台输入，直到输入 q 退出为止
        new Thread(()->{

            //读取控制台输入
            Scanner scanner =  new Scanner(System.in);
            while (true){
                String line = scanner.nextLine();

                //如果输入 q 则退出程序，关闭channel
                if("q".equals(line)){
                    channel.close();
//                    log.info("线程关闭（写这里也不合适，因为channel.close()是异步方法）");
                    break;
                }

                //如果输入不为 q 则将信息发送到服务端
                channel.writeAndFlush(line);
            }

        }, "input-thread").start();

        //实际这句话会先打印，因为是main线程打印
//        log.info("线程关闭了");

        //3. 使用 closeFuture 关闭
        ChannelFuture closeFuture = channel.closeFuture();
        log.info("等待关闭...");

        //同步处理关闭, 阻塞线程，这样才能正常处理关闭事件
        closeFuture.sync();
        log.info("线程关闭了");
    }
}
