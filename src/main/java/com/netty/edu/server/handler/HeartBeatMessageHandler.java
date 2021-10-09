package com.netty.edu.server.handler;

import com.netty.edu.message.PingMessage;
import com.netty.edu.message.PongMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HeartBeatMessageHandler
 * @Author zlc
 * @Date 2021/10/9 下午4:58
 * @Description HeartBeatMessageHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class HeartBeatMessageHandler extends SimpleChannelInboundHandler<PingMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PingMessage pingMessage) throws Exception {
        channelHandlerContext.channel().writeAndFlush(new PongMessage());
    }
}
