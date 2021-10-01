package com.netty.edu.server.session;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @ClassName GroupSessionMemoryImpl
 * @Author zlc
 * @Date 2021/10/1 下午11:21
 * @Description GroupSessionMemoryImpl
 * @Version 1.0
 */
@Slf4j
public class GroupSessionMemoryImpl implements GroupSession {

    private final Map<String, Group> groupMap = new HashMap<>();


    @Override
    public Group createGroup(String name, Set<String> members) {
        //判断当前群是否已存在
        if (!groupMap.containsKey(name)) {
            groupMap.put(name, new Group(name, members));
            return groupMap.get(name);
        }
        //群已存在时再创建返回null
        return null;
    }

    @Override
    public Group joinMember(String name, String userName) {
        if (!groupMap.containsKey(name)) {
            Group group = groupMap.get(name);
            Set<String> members = group.getMembers();
            if (members.contains(userName)) {
                //重复加入群
                return null;
            }
            members.add(userName);
            group.setMembers(members);
            groupMap.put(name, group);
            return group;
        }

        return null;
    }

    @Override
    public Group removeMember(String name, String userName) {
        Group group = groupMap.get(name);
        if (group == null) {
            return null;
        }
        Set<String> members = group.getMembers();
        if (!members.contains(userName)) {
            return null;
        }
        members.remove(userName);
        group.setMembers(members);
        groupMap.put(name, group);
        return group;
    }

    @Override
    public Group removeGroup(String name) {
        return null;
    }

    @Override
    public Set<String> getMembers(String name) {
        Group group = groupMap.get(name);
        return group == null ? null : group.getMembers();
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        Session session = SessionFactory.getSession();
        List<Channel> channelList = new ArrayList<>();
        Group group = groupMap.get(name);
        if (group == null) {
            return null;
        }
        group.getMembers().forEach((member) -> {
            Channel channel = session.getChannel(member);
            if (channel == null || !channel.isOpen()) {
                log.debug("用户: {}，不在线", member);
            } else {
                channelList.add(channel);
            }
        });
        return channelList;
    }
}
