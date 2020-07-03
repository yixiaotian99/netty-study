package com.xiao.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author sunjinwei
 * @Date 2020-07-03 14:06
 * @Description 直接修改堆外内存，修改文件内容
 **/
public class MappedByteBufferTest {

    public static void main(String[] args) throws IOException {


        //使用随机文件访问，可访问文件任意的位置
        RandomAccessFile accessFile = new RandomAccessFile("01.txt", "rw");

        //获取文件通道
        FileChannel fileChannel = accessFile.getChannel();

        //文件内存映射  读写模式，从索引0开始，映射5个字节
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        //修改内存中值
        mappedByteBuffer.put(0, (byte)'A');
        mappedByteBuffer.put(1, (byte)'B');
        mappedByteBuffer.put(2, (byte)'C');
        mappedByteBuffer.put(3, (byte)'D');
        mappedByteBuffer.put(4, (byte)'E');
//        mappedByteBuffer.put(5, (byte)'F');  //最多修改5个，如果修改第6个会报下标越界

        //关闭通道
        accessFile.close();
        fileChannel.close();

    }

}
