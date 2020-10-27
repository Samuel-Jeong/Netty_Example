package com.netty.channel.handler;

import com.netty.channel.NettyChannelManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    private final ByteBuf message;
    ;

    public NettyClientHandler() {
        message = Unpooled.buffer(256);
        byte[] str = "Hello world!".getBytes();
        message.writeBytes(str);
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        logger.info("Success to connect with {}", channelHandlerContext.channel().remoteAddress());
        channelHandlerContext.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        int rBytes = buf.readableBytes();
        logger.info("msg:{}({})", buf.toString(CharsetUtil.UTF_8), rBytes);
        channelHandlerContext.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        NettyChannelManager.getInstance().stopClient();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }
}