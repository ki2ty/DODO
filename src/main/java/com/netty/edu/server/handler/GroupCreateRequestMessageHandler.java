package com.netty.edu.server.handler;

import com.netty.edu.message.GroupCreateRequestMessage;
import com.netty.edu.message.GroupCreateResponseMessage;
import com.netty.edu.server.session.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @ClassName GroupCreateRequestMessageHandler
 * @Author zlc
 * @Date 2021/10/2 上午2:11
 * @Description GroupCreateRequestMessageHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, GroupCreateRequestMessage groupCreateRequestMessage) throws Exception {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Session session = SessionFactory.getSession();
        String creator = groupCreateRequestMessage.getCreator();
        Set<String> members = groupCreateRequestMessage.getMembers();
        String groupName = groupCreateRequestMessage.getGroupName();
        //判断当前创建人是否在members中
        boolean contains = members.contains(creator);
        log.debug("group creator: {} is{} in members", creator, contains ? "" : " not");
        Group group = groupSession.createGroup(groupName, members);
        if (group != null) {
            //通知所有人群已创建
            members.forEach((member) -> {
                Channel channel = session.getChannel(member);
                if (channel != null && channel.isOpen()) {
                    channel.writeAndFlush(new GroupCreateResponseMessage(creator, groupName));
                } else {
                    log.debug("群用户: {}，不在线，未通知其群已创建", member);
                }
            });
            //是否需要通知创建者群创建成功
            if (!contains) {
                session.getChannel(creator).writeAndFlush(new GroupCreateResponseMessage(creator, groupName));
            }
        } else {
            session.getChannel(creator).writeAndFlush(new GroupCreateResponseMessage(false, "群已存在，创建失败"));
        }
    }
}
