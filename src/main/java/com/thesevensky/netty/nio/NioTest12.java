package com.thesevensky.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 *Selector 服务器端
 */

public class NioTest12 {
    public static void main(String[] args) throws Exception {
        int[] ports = new int[5];
        ports[0] = 50000;
        ports[1] = 50001;
        ports[2] = 50002;
        ports[3] = 50003;
        ports[4] = 50004;

        Selector selector = Selector.open();
        System.out.println(selector.getClass());

        for (int i = 0; i < ports.length; ++i) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //是否设置为阻塞 false为不阻塞
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            //绑定
            serverSocket.bind(address);
            //arg2 对什么操作感兴趣的
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("监听端口:   " + ports[i]);
        }

        for(;;) {
            int numbers = selector.select();
            System.out.println("numbers : " + numbers);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys : " + selectionKeys);
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if(next.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel)next.channel();
                    //这里表示真正的连接的对象
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector, SelectionKey.OP_READ);

                    //删除监听的注册事件
                    iterator.remove();
                    System.out.println("获得客户端连接 : " + socketChannel);
                } else if(next.isReadable()) {
                    readData(next);
                    //一定要remove掉　不然又会监听这个事件
                    iterator.remove();
                }
            }
        }
    }
    public static void readData(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
        int byteRead = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        for(;;) {

            byteBuffer.clear();
            int read = socketChannel.read(byteBuffer);

            if(read <= 0) break;
            //读写反转
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteRead += read;
        }
        System.out.println("读取 : " + byteRead + ", 来自于 : " + socketChannel);
    }
}
