package com.netty.edu.server.session;

import io.netty.channel.Channel;

/**
 * @ClassName Session
 * @Author zlc
 * @Date 2021/10/1 下午10:44
 * @Description Session 会话管理接口
 * @Version 1.0
 */
public interface Session {

    /**
     * 绑定会话
     *
     * @param channel  哪个channel要绑定
     * @param userName 绑定的用户
     */
    void bind(Channel channel, String userName);

    /**
     * 解绑会话
     *
     * @param channel 哪个channel要解绑
     */
    void unbind(Channel channel);

    /**
     * 获取channel中的属性值
     *
     * @param channel 哪个channel
     * @param name    属性名称
     * @return 属性值
     */
    Object getAttribute(Channel channel, String name);

    /**
     * 设置channel的属性
     *
     * @param channel 哪个channel
     * @param name    属性名称
     * @param value   属性值
     */
    void setAttribute(Channel channel, String name, Object value);

    /**
     * 获取用户绑定的channel
     *
     * @param userName 用户名
     * @return channel
     */
    Channel getChannel(String userName);

    /**
     * 获取channel对应的用户
     * @param channel   哪个channel
     * @return          用户名
     */
    String getUser(Channel channel);


}
