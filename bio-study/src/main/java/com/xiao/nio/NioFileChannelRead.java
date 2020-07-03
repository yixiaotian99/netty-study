package com.xiao.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author sunjinwei
 * @Date 2020-07-03 13:42
 * @Description 使用 FileChannel 读取文件
 **/
public class NioFileChannelRead {


    public static void main(String[] args) throws IOException {


        //获取输入流
        FileInputStream fileInputStream = new FileInputStream("01.txt");

        //获取通道
        FileChannel fileChannel = fileInputStream.getChannel();

        //定义缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(fileInputStream.available());


        //关联文件通道与缓冲区
        fileChannel.read(byteBuffer);

        //读写切换
        byteBuffer.flip();

        //获取文件内容
        String res = new String(byteBuffer.array());
        System.out.println("读取文件内容: " + res);

        fileInputStream.close();
    }

}
