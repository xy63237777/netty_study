package com.thesevensky.netty.gRPC;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class RPCService {
    private Server server;

    private void start() throws Exception {
        this.server = ServerBuilder.forPort(8899)
                .addService(new StudentServiceImpl())
                .build().start();
        System.out.println("server start");
        //关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("关闭jvm");
            RPCService.this.stop();
        }));
        System.out.println("执行到这里");
    }

    private void stop() {
        if (null != this.server) {
            this.server.shutdown();
        }
    }

    private void awaitTermination() throws InterruptedException {
        //防止服务启动就直接退出
        if(null != this.server) {
            this.server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        RPCService rpcService = new RPCService();
        rpcService.start();
        rpcService.awaitTermination();
    }
}
