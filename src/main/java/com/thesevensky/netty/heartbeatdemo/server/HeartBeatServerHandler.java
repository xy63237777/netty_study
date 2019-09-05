package com.thesevensky.netty.heartbeatdemo.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;

            switch (event.state()) {
                case WRITER_IDLE:
                    eventType = "写空闲"; break;
                case READER_IDLE:
                    eventType = "读空闲"; break;
                case ALL_IDLE:
                    eventType = "读写空闲"; break;
            }
            System.out.println(ctx.channel().remoteAddress() + " 超时时间 " + eventType);
            ctx.channel().close();
        }
    }
}
