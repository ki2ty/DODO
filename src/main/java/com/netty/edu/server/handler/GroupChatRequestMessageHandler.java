package com.netty.edu.server.handler;

import com.netty.edu.message.GroupChatRequestMessage;
import com.netty.edu.message.GroupChatResponseMessage;
import com.netty.edu.server.session.GroupSession;
import com.netty.edu.server.session.GroupSessionFactory;
import com.netty.edu.server.session.Session;
import com.netty.edu.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName GroupChatRequestHandler
 * @Author zlc
 * @Date 2021/10/2 上午2:05
 * @Description GroupChatRequestHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupChatRequestMessage groupChatRequestMessage) throws Exception {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Session session = SessionFactory.getSession();
        List<Channel> membersChannel = groupSession.getMembersChannel(groupChatRequestMessage.getGroupName());
        if (membersChannel == null) {
            log.debug("group not exist");
            channelHandlerContext.writeAndFlush(new GroupChatResponseMessage(false, "group not exist"));
        }else{
            membersChannel.forEach((channel) -> {
                channel.writeAndFlush(new GroupChatResponseMessage(groupChatRequestMessage.getFrom(),groupChatRequestMessage.getContent()));
            });
        }
    }
}
