package com.thesevensky.netty.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIOServer {
    public static void main(String[] args) throws Exception {
        InetSocketAddress address = new InetSocketAddress(8899);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        //开启这个状态. 如果想绑定某个端口处于TIME_WAIT(关闭处理TIME_WAIT的状态)的时候也可以绑定到这个端口
        serverSocket.setReuseAddress(true);
        serverSocket.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel channel = serverSocketChannel.accept();
            //阻塞的 因为没有Selector所以阻塞就行了
            channel.configureBlocking(true);

            int readCount = 0;
            while(-1 != readCount) {
                try{
                    readCount = channel.read(byteBuffer);
                    //重放操作
                    byteBuffer.rewind();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
