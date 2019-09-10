package com.thesevensky.netty.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        String fileName = "/opt/goland-2019.1.3.tar.gz";
        socketChannel.connect(new InetSocketAddress("localhost", 8899));
        socketChannel.configureBlocking(true);

        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //将这个文件关联的channel写到可写的channel中,会从给定的位置开始读取给定的大小为止
        //如果是非阻塞的 可能也不会读取想要数量的字节
        //这个方法 很多操作系统会将文件系统将字节写到目标通道中 零拷贝
        long startTime = System.currentTimeMillis();
        long transferTo = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送总字节数: " + transferTo + " , 耗时: " + (System.currentTimeMillis() - startTime) + "毫秒");
    }
}
