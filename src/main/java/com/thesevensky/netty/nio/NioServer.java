package com.thesevensky.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class NioServer {

    //视频里讲的　但是感觉不好
    private static Map<String, SocketChannel> clientMap = new ConcurrentHashMap<>();

    private final static int BUFFER_SIZE = 1024;

    private static Map<SocketChannel, String> clientNewMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            //堵塞　select是获取的事件的集合数量
            int select = selector.select();
            try {
                work(selector);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void work(Selector selector) throws Exception {
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        selectionKeys.forEach(selectionKey -> {
            try{
                if(selectionKey.isAcceptable()) {
                    doAcceptable(selectionKey, selector);
                } else if(selectionKey.isReadable()) {
                    doReadable(selectionKey);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            selectionKeys.remove(selectionKey);
        });
    }

    private static void doAcceptable(SelectionKey selectionKey, Selector selector) throws Exception {
        final SocketChannel client;
        //获取到当前事件发生在那个ServerSocketChannel上
        ServerSocketChannel socketChannel = (ServerSocketChannel) selectionKey.channel();
        client = socketChannel.accept();
        client.configureBlocking(false);
        //注册到selector上
        client.register(selector, SelectionKey.OP_READ);
        //视频里讲的　感觉不太好
        String key = "[" + UUID.randomUUID().toString() + "]";
        clientMap.put(key, client);

        clientNewMap.put(client, key);
    }

    /**
     *　读取
     */
    private static void doReadable(SelectionKey selectionKey) throws IOException {
        //这里有一些东西没有处理
        final SocketChannel client = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        int count = client.read(byteBuffer);
        if(count > 0) {
            byteBuffer.flip();
            Charset charset = Charset.forName("utf-8");
            String receivedMessage = String.valueOf(charset.decode(byteBuffer).array());
            System.out.println(client +  " : " + receivedMessage);
            doRepeat(receivedMessage, selectionKey);
        }
    }

    /**
     * 转发
     */
    private static void doRepeat(String message, SelectionKey sendKey) throws IOException {
        String key = clientNewMap.get((SocketChannel) sendKey.channel());
        for(Map.Entry<SocketChannel, String> entry : clientNewMap.entrySet()) {
            ByteBuffer wirerBuffer = ByteBuffer.allocate(BUFFER_SIZE + 100);
            if(entry.getKey().equals((SocketChannel) sendKey.channel())) {
                wirerBuffer.put(("你自己 : " + message).getBytes());
            } else {
                wirerBuffer.put((entry.getValue() + " : " + message).getBytes());
            }
            wirerBuffer.flip();
            entry.getKey().write(wirerBuffer);
        }

    }
}


