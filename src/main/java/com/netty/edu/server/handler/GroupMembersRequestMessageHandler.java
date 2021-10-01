package com.netty.edu.server.handler;

import com.netty.edu.message.GroupMembersRequestMessage;
import com.netty.edu.message.GroupMembersResponseMessage;
import com.netty.edu.server.session.GroupSession;
import com.netty.edu.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @ClassName GroupMembersRequestMessage
 * @Author zlc
 * @Date 2021/10/2 上午2:36
 * @Description GroupMembersRequestMessage
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupMembersRequestMessageHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupMembersRequestMessage groupMembersRequestMessage) throws Exception {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        String groupName = groupMembersRequestMessage.getGroupName();
        Set<String> members = groupSession.getMembers(groupName);
        if (members == null) {
            //群不存在
            channelHandlerContext.writeAndFlush(new GroupMembersResponseMessage(false, "群不存在"));
        } else {
            channelHandlerContext.writeAndFlush(new GroupMembersResponseMessage(members));
        }
    }
}
