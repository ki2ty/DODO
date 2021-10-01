package com.netty.edu.server.handler;

import com.netty.edu.message.GroupQuitRequestMessage;
import com.netty.edu.message.GroupQuitResponseMessage;
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
 * @ClassName GroupQuitRequestMessageHandler
 * @Author zlc
 * @Date 2021/10/2 上午3:00
 * @Description GroupQuitRequestMessageHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupQuitRequestMessage groupQuitRequestMessage) throws Exception {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();

        String groupName = groupQuitRequestMessage.getGroupName();
        String userName = groupQuitRequestMessage.getUserName();

        Group group = groupSession.removeMember(groupName, userName);
        if (group != null) {
            //向全部在线群成员发送消息，通知退出了一名群成员
            List<Channel> membersChannel = groupSession.getMembersChannel(group.getGroupName());
            membersChannel.forEach((channel) -> channel.writeAndFlush(new GroupQuitResponseMessage(userName)));
        } else {
            channelHandlerContext.writeAndFlush(new GroupQuitResponseMessage(false, "退出群失败"));
        }
    }
}
