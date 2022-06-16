package com.xiao.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @className: EventLoopServer
 * @description: TODO 类描述
 * @author: sunjinwei
 * @date: 2022/6/16 23:56
 * @see https://www.bilibili.com/video/BV1py4y1E7oA?p=60&vd_source=c7d04e9bc3c6730e7b997e36586b7383
 **/
@Slf4j
public class EventLoopServer3 {

    public static void main(String[] args) {

        //指定非 nio 线程组
        DefaultEventLoop defaultEventLoop = new DefaultEventLoop();

        new ServerBootstrap()
                //再次分工细化
                //组group分为 boss 与 worker 线程
                //boss只负责 ServerSockerChannel 中的 accept 事件
                //worker只负责 SockerChannel 中的 读写 事件
                //如果某个任务处理时间过长，那会占用 NioEventLoopGroup 线程，导致其他读写变慢，可以把耗时长的任务将给非 nio 线程来执行
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //增加通道
                        ch.pipeline().addLast("nioHandler", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //默认如果没有解析器，获取到的 msg 是 ByteBuf 类型
                                ByteBuf buf = (ByteBuf) msg;

                                //将 ByteBuf 类型转为字符串，注意要保证客户端与服务端编码一致，这里使用默认编码
                                String s = buf.toString(Charset.defaultCharset());
                                log.info("获取到客户端发送过来消息, msg:{}", s); //00:29:06.884 [nioEventLoopGroup-4-2] INFO com.xiao.c2.EventLoopServer3 - 获取到客户端发送过来消息, msg:hello

                                //需要触发任务
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(defaultEventLoop, "Handler", new ChannelInboundHandlerAdapter() {

                            //指定非 nio 事件循环组，解决耗时长的任务
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //默认如果没有解析器，获取到的 msg 是 ByteBuf 类型
                                ByteBuf buf = (ByteBuf) msg;

                                //将 ByteBuf 类型转为字符串，注意要保证客户端与服务端编码一致，这里使用默认编码
                                String s = buf.toString(Charset.defaultCharset());
                                log.info("获取到客户端发送过来消息, msg:{}", s); //00:29:06.886 [defaultEventLoop-1-1] INFO com.xiao.c2.EventLoopServer3 - 获取到客户端发送过来消息, msg:hello
                            }
                        });
                    }
                })
                .bind(8081);
    }
}
