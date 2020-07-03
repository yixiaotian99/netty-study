package com.xiao.nio;

import java.nio.ByteBuffer;

/**
 * @Author sunjinwei
 * @Date 2020-07-02 22:59
 * @Description TODO
 **/
public class BasicByteBuffer {


    public static void main(String[] args) {
//        testNoFlip();
        testFlip();
//        testRewind();
    }

    private static void testNoFlip() {
        //创建一个容量为 5的 byte 数组
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 2);
        byteBuffer.put((byte) 3);

        System.out.println("容量: " + byteBuffer.capacity());

        for (int i = 0; i < byteBuffer.capacity(); i++) {

            System.out.println("读取: " + byteBuffer.get());  //异常 java.nio.BufferUnderflowException
        }
    }

    private static void testFlip() {
        //创建一个容量为 5的 byte 数组
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 2);
        byteBuffer.put((byte) 3);
//        byteBuffer.put((byte) 4);
//        byteBuffer.put((byte) 5);

        System.out.println("容量: " + byteBuffer.capacity());

        byteBuffer.flip();  //将原来put写操作切换到读模式，position从0开始读取
        for (int i = 0; i < byteBuffer.capacity(); i++) {

            System.out.println("读取: " + byteBuffer.get());  //异常 java.nio.BufferUnderflowException
        }
    }


    private static void testRewind() {
        //创建一个容量为 5的 byte 数组
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        byteBuffer.put((byte) 1);
        byteBuffer.put((byte) 2);
        byteBuffer.put((byte) 3);

        System.out.println("容量: " + byteBuffer.capacity());

        byteBuffer.rewind(); //仅将position从0开始，将未填充满的位置填充为0
        for (int i = 0; i < byteBuffer.capacity(); i++) {

            System.out.println("读取: " + byteBuffer.get());  //输出 12300
        }
    }


}
