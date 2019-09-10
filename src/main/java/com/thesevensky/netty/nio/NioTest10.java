package com.thesevensky.netty.nio;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 文件锁
 */
public class NioTest10 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest10.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        //arg1 锁的起始位置  arg2 锁的长度 arg3是否为共享锁
        FileLock lock = channel.lock(3, 6, true);

        System.out.println("有效性 : " + lock.isValid());
        System.out.println("是否为共享锁 : " + lock.isShared());

        //释放锁
        lock.release();

        randomAccessFile.close();
    }
}
