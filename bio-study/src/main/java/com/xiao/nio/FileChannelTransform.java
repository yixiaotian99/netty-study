package com.xiao.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @Author sunjinwei
 * @Date 2020-07-03 14:01
 * @Description 使用通道进行文件拷贝
 **/
public class FileChannelTransform {

    public static void main(String[] args) throws IOException {

        //读取 01.txt 文件
        FileInputStream fileInputStream = new FileInputStream("01.txt");
        FileChannel fileInChannel = fileInputStream.getChannel();


        //准备写入的 02.txt 文件
        FileOutputStream fileOutputStream = new FileOutputStream("03.txt");
        FileChannel fileOutChannel = fileOutputStream.getChannel();


        //拷贝文件
        fileOutChannel.transferFrom(fileInChannel, 0 , fileInChannel.size());


        //关闭通道和流
        fileOutChannel.close();
        fileInChannel.close();
        fileOutputStream.close();
        fileInputStream.close();
    }
}
