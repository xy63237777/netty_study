package com.thesevensky.netty.nio;

import java.nio.ByteBuffer;

/**
 * 只读buffer
 */

public class NioTest7 {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        System.out.println(byteBuffer.getClass());
        for(int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte)i);
        }

        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        readOnlyBuffer.position(0);
        readOnlyBuffer.put((byte) 2);//抛异常

    }
}
