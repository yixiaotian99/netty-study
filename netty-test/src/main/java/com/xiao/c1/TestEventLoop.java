package com.xiao.c1;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @className: TestEventLoop
 * @description: TODO 类描述
 * @author: sunjinwei
 * @date: 2022/6/16 23:32
 * @see https://www.bilibili.com/video/BV1py4y1E7oA?p=59&vd_source=c7d04e9bc3c6730e7b997e36586b7383
 **/
@Slf4j
public class TestEventLoop {

    public static void main(String[] args) {
        //1. 创建事件循环组 如果不传线程数默认取系统可用核数*2， io事件，可执行 普通任务、定时任务
        EventLoopGroup group = new NioEventLoopGroup(2);

        //与 NioEventLoopGroup 不能的是不能指定线程数，不能执行io事件，可执行 普通任务，定时任务
        EventLoopGroup eventLoopGroup2 = new DefaultEventLoop();

        //2. 获取下一个事件循环对象，如果有2个线程，就会循环使用线程
        log.info("当前线程1:{}", group.next());  //io.netty.channel.nio.NioEventLoop@61ca2dfa
        log.info("当前线程2:{}", group.next());  //io.netty.channel.nio.NioEventLoop@4b53f538
        log.info("当前线程3:{}", group.next());  //io.netty.channel.nio.NioEventLoop@61ca2dfa
        log.info("当前线程4:{}", group.next());  //io.netty.channel.nio.NioEventLoop@4b53f538

        //3. 执行普通任务，继承线程池，因此有线程池所有方法
        group.execute(()->{
            log.info("子线程输出:ok");
        });
        log.info("主线程输出---ok");

        //4.执行定时任务  延时2秒之后，每1秒输出一次信息
        group.scheduleAtFixedRate(()->{
            log.info("执行定时任务");
        }, 2, 1, TimeUnit.SECONDS);
    }
}
