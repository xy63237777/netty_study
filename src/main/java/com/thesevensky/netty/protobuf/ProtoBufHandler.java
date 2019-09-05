package com.thesevensky.netty.protobuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtoBufHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.PersonType) {
            System.out.println(msg.getPerson());
            System.out.println(msg.getPerson().getName());
            System.out.println(msg.getPerson().getAge());
            System.out.println(msg.getPerson().getAddress());
        } else if(dataType == MyDataInfo.MyMessage.DataType.DogType) {
            System.out.println(msg.getDog().getAge());
            System.out.println(msg.getDog().getName());
        } else {
            System.out.println(msg.getCat().getCity());
            System.out.println(msg.getCat().getName());
        }
    }
}
