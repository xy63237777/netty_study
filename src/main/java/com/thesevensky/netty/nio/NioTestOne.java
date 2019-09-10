package com.thesevensky.netty.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

public class NioTestOne {
    public static void main(String[] args) {
        //分配一个大小为10的缓冲区
        IntBuffer intBuffer = IntBuffer.allocate(10);

        for(int i = 0; i < intBuffer.capacity(); i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            intBuffer.put(randomNumber);
        }

        //转换标志位
        intBuffer.flip();

        while(intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
