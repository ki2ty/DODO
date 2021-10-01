package com.netty.edu.server.handler;

import com.netty.edu.message.GroupJoinRequestMessage;
import com.netty.edu.message.GroupJoinResponseMessage;
import com.netty.edu.server.session.Group;
import com.netty.edu.server.session.GroupSession;
import com.netty.edu.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName GroupJoinRequestMessageHandler
 * @Author zlc
 * @Date 2021/10/2 上午2:41
 * @Description GroupJoinRequestMessageHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupJoinRequestMessage groupJoinRequestMessage) throws Exception {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();

        String groupName = groupJoinRequestMessage.getGroupName();
        String userName = groupJoinRequestMessage.getUserName();

        Group group = groupSession.joinMember(groupName, userName);
        if (group != null) {
            //向全部在线群成员发送消息，通知新加入了一名群成员
            List<Channel> membersChannel = groupSession.getMembersChannel(group.getGroupName());
            membersChannel.forEach((channel) -> channel.writeAndFlush(new GroupJoinResponseMessage(userName)));
        } else {
            channelHandlerContext.writeAndFlush(new GroupJoinResponseMessage(false, "加入群失败"));
        }
    }
}
