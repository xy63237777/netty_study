package com.thesevensky.netty.nio;

import java.nio.ByteBuffer;

public class NioTest6 {

    public static void main(String[] args) {
        //byteBuffer的分片操作和Go语言中的切片是一样的 共享一个底层数组
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        for(int i = 0; i < byteBuffer.capacity(); ++i) {
            byteBuffer.put((byte)i);
        }

        byteBuffer.position(2);
        byteBuffer.limit(6);
        ByteBuffer slice = byteBuffer.slice();


    }
}
