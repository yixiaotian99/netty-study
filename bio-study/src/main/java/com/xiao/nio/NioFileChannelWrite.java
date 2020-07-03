package com.xiao.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author sunjinwei
 * @Date 2020-07-03 11:27
 * @Description 通过使用 NOI 中 FileChannel 写入文本
 **/
public class NioFileChannelWrite {


    public static void main(String[] args) throws IOException {

        //指定写入的文件
        FileOutputStream fileInputStream = new FileOutputStream("01.txt");


        //获取 FileChannel
        FileChannel fileChannel = fileInputStream.getChannel();


        //获取一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(512);

        //写入的字符串
        String wstr = "hello, NIO 你好";
        buffer.put(wstr.getBytes());

        //非常非常非常重要，进行缓冲区读写切换，非常非常非常重要，进行缓冲区读写切换
        buffer.flip();

        //将缓冲区数据读入
        fileChannel.write(buffer);

        //关闭流
        fileInputStream.close();
        System.out.println("写入文件结束");
        fileChannel.close();

    }

}
