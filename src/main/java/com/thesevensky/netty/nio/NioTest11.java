package com.thesevensky.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 关于Buffer的Scattering 与 Gathering
 */
public class NioTest11 {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);

        int messageLength = 2 + 3 + 4;
        ByteBuffer[] byteBuffers = new ByteBuffer[3];
        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);

        SocketChannel socketChannel = serverSocketChannel.accept();

        for (;;) {
            int byteRead = 0;
            while(byteRead < messageLength) {
                long r = socketChannel.read(byteBuffers);
                byteRead += r;
                System.out.println("byteRead : " + byteRead);

                Arrays.asList(byteBuffers).stream().map(buffer -> "position" + buffer.position()
                        + ", limit: " + buffer.limit()).forEach(System.out::println);

            }
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.flip();
            });
            long byteWritten = 0;
            while(byteWritten < messageLength) {
                long rr = socketChannel.write(byteBuffers);
                byteWritten += rr;
            }
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });
            System.out.println("bytesRead : " + byteRead + ", bytesWritten : " + byteWritten +
                    ", messageLength : " + messageLength);
        }
    }
}
