package com.xiao.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author sunjinwei
 * @Date 2020-07-03 13:50
 * @Description 使用 FileChannel 读取 01.txt 写入 02.txt
 **/
public class NOIFileChannelRead2Write {


    public static void main(String[] args) throws IOException {

        //读取 01.txt 文件
        FileInputStream fileInputStream = new FileInputStream("01.txt");
        FileChannel fileInChannel = fileInputStream.getChannel();


        //准备写入的 02.txt 文件
        FileOutputStream fileOutputStream = new FileOutputStream("02.txt");
        FileChannel fileOutChannel = fileOutputStream.getChannel();

        //读取与写入共用一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(fileInputStream.available());


        //先循环读取
        while (true) {

            //非常非常非常重要，要不然就死循环了，清空位置
            byteBuffer.clear();

            //将文件内容读入缓冲区
            int read = fileInChannel.read(byteBuffer);

            if (read == -1) {
                System.out.println("读取结束");
                break;
            }


            //读写切换
            byteBuffer.flip();

            //将缓冲区文件写入新文件
            fileOutChannel.write(byteBuffer);
        }

        fileOutputStream.close();
        fileInputStream.close();

    }

}
