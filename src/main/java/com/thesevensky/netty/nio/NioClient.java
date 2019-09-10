package com.thesevensky.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient {

    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            selectionKeys.forEach(selectionKey -> {
                try {
                    boolean isConnected = false;
                    if(selectionKey.isConnectable()) {
                        isConnected = doConnectable(selectionKey);
                        if(isConnected) {
                            doWorker(selectionKey);
                        }
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if(selectionKey.isReadable()) {
                        doReadable(selectionKey);
                    }
                    //selectionKeys.remove(selectionKey);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            });
        selectionKeys.clear();
        }
    }
    private static boolean doConnectable(SelectionKey selectionKey) throws IOException {
        SocketChannel clent = (SocketChannel) selectionKey.channel();
        boolean flag = false;
        //连接是否处在进行的状态
        if(clent.isConnectionPending()) {
            //完成连接有返回值的　可以试试重试
            flag = clent.finishConnect();

            ByteBuffer writeBuffer = ByteBuffer.allocate(100);
            writeBuffer.put((LocalDateTime.now() + "连接成功").getBytes());
            writeBuffer.flip();
            clent.write(writeBuffer);
        }
        System.out.println(flag);
        return flag;
    }

    private static void doWorker(final SelectionKey selectionKey) throws IOException {
        ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
        executorService.submit(() -> {
            try{
                SocketChannel client = (SocketChannel)selectionKey.channel();
                ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                while (true) {
                    writeBuffer.clear();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    String sendMessage = bufferedReader.readLine();
                    writeBuffer.put(sendMessage.getBytes());
                    writeBuffer.flip();
                    client.write(writeBuffer);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    private static void doReadable(SelectionKey selectionKey) throws IOException {
        SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        int count = client.read(readBuffer);
        if(count > 0) {
            String receivedMessage = new String(readBuffer.array(), 0, count);
            System.out.println(receivedMessage);
        }
    }
}
