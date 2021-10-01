package com.netty.edu.server.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * @ClassName GroupSession
 * @Author zlc
 * @Date 2021/10/1 下午10:35
 * @Description GroupSession 聊天群组会话管理接口
 * @Version 1.0
 */
public interface GroupSession {

    /**
     * 创建聊天群组
     *
     * @param name    群组名称
     * @param members 用户名称集合
     * @return 创建成功返回组对象，失败返回空
     */
    Group createGroup(String name, Set<String> members);

    /**
     * 加入群组
     *
     * @param name     加入群组的名称
     * @param userName 加入成员用户名称
     * @return 如果组不存在或者加入失败返回null，否则返回组对象
     */
    Group joinMember(String name, String userName);

    /**
     * 移除群组成员
     *
     * @param name     群组名称
     * @param userName 要被移除的用户名
     * @return 群组不存在或用户不存在则返回null，否则返回组对象
     */
    Group removeMember(String name, String userName);

    /**
     * 移除聊天组
     *
     * @param name 群组名称
     * @return 移除失败返回null，成功返回组对象
     */
    Group removeGroup(String name);

    /**
     * 获取聊天组当前所有成员名称
     *
     * @param name 群组名称
     * @return 组不存在返回null，否则返回成员名称集合
     */
    Set<String> getMembers(String name);

    /**
     * 获取当前组成员的所有channel集合
     *
     * @param name 群组名称
     * @return channel集合
     */
    List<Channel> getMembersChannel(String name);

}
