package com.thesevensky.netty.protobuf;

import com.thesevensky.netty.chatdemo.client.ChatClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestClient {
    public static void main(String[] args) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try{
            /**
             * 这里和Server不同的是一个是ChildHandler 和 Handler
             * ChildHandler对应的是workerGroup 而 Handler对应的则是 boosGroup
             */
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new MyClientInitializer());

            Channel channel = bootstrap.connect("localhost", 8899).sync().channel();
            channel.closeFuture().sync();

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
