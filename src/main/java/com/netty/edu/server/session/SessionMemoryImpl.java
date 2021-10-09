package com.netty.edu.server.session;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName SessionMemoryImpl
 * @Author zlc
 * @Date 2021/10/1 下午10:58
 * @Description SessionMemoryImpl
 * @Version 1.0
 */
@Slf4j
public class SessionMemoryImpl implements Session {

    private Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    private Map<Channel, String> channelUserMap = new ConcurrentHashMap<>();
    private Map<Channel, Map<String, Object>> channelAttributesMap = new ConcurrentHashMap<>();


    @Override
    public void bind(Channel channel, String userName) {
        userChannelMap.put(userName, channel);
        channelUserMap.put(channel, userName);
        channelAttributesMap.put(channel, new ConcurrentHashMap<>());
    }



    @Override
    public void unbind(Channel channel) {
        String userName = channelUserMap.remove(channel);
        if(userName != null){
            userChannelMap.remove(userName);
            channelAttributesMap.remove(channel);
        }else{
            log.debug("未登录的客户端进行unbind");
        }
    }

    @Override
    public Object getAttribute(Channel channel, String name) {
        Map<String, Object> objectMap = channelAttributesMap.get(channel);

        return objectMap == null ? null : objectMap.get(name);
    }

    @Override
    public void setAttribute(Channel channel, String name, Object value) {
        channelAttributesMap.get(channel).put(name, value);
    }

    @Override
    public Channel getChannel(String userName) {
        return userChannelMap.get(userName);
    }

    @Override
    public String getUser(Channel channel) {
        return channelUserMap.get(channel);
    }
}
