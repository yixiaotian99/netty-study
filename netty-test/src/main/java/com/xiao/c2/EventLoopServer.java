package com.xiao.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
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
public class EventLoopServer {

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        //增加通道
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //默认如果没有解析器，获取到的 msg 是 ByteBuf 类型
                                ByteBuf buf = (ByteBuf) msg;

                                //将 ByteBuf 类型转为字符串，注意要保证客户端与服务端编码一致，这里使用默认编码
                                String s = buf.toString(Charset.defaultCharset());
                                log.info("获取到客户端发送过来消息, msg:{}", s);
                            }
                        });
                    }
                })
                .bind(8081);
    }
}
