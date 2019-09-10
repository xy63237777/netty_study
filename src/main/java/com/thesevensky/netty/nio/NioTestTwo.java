package com.thesevensky.netty.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioTestTwo {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("NioTest2.txt");
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        fileChannel.read(byteBuffer);

        //读写转换
        byteBuffer.flip();

        while(byteBuffer.remaining() > 0) {
            byte b = byteBuffer.get();
            System.out.println("Character : " + (char)b);
        }
        fileInputStream.close();
    }
}
