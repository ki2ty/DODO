package com.netty.edu.server.handler;

import com.netty.edu.message.ChatRequestMessage;
import com.netty.edu.message.ChatResponseMessage;
import com.netty.edu.server.session.Session;
import com.netty.edu.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ChatRequestMessageHandler
 * @Author zlc
 * @Date 2021/10/2 上午1:43
 * @Description ChatRequestMessageHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ChatRequestMessage chatRequestMessage) throws Exception {
        String to_user = chatRequestMessage.getTo();
        Session session = SessionFactory.getSession();
        Channel to_channel = session.getChannel(to_user);
        if (to_channel != null && to_channel.isOpen()) {
            //在线
            to_channel.writeAndFlush(new ChatResponseMessage(chatRequestMessage.getFrom(), chatRequestMessage.getContent()));
        } else {
            //不在线
            channelHandlerContext.channel().writeAndFlush(new ChatResponseMessage(false, "对方不在线"));
        }
    }
}
