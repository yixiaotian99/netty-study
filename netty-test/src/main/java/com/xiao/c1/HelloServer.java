package com.xiao.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @className: HelloServer
 * @description: TODO 类描述
 * @author: sunjinwei
 * @date: 2022/6/15 23:37
 * @see https://www.bilibili.com/video/BV1py4y1E7oA?p=56&vd_source=c7d04e9bc3c6730e7b997e36586b7383
 * @see https://nyimac.gitee.io/2021/04/25/Netty%E5%9F%BA%E7%A1%80/#Netty
 **/
public class HelloServer {

    public static void main(String[] args) {
        //1. 启动器，负责装配 netty 组件
        new ServerBootstrap()
                //2.创建group组(线程池+selector)，包含 BossEventLoop、WorkerEventLoop
                .group(new NioEventLoopGroup())
                //3.选择服务器的 ServerSocket 实现
                .channel(NioServerSocketChannel.class)
                //4.boss负责处理连锁，worker负责处理读写，决定由handler指定
                .childHandler(
                        //5.channel为与客户端读取的通道 Initializer为初始化方法，负责添加其他handler
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                //6.添加具体的handler
                                ch.pipeline().addLast(new StringDecoder()); //将客户端发送来的 ByteBuf 转为 String
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() { //自定义handler
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("输出:" + msg);
                                    }
                                });
                            }
                        })
                .bind(8088);

    }
}
