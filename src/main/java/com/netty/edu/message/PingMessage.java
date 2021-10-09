package com.netty.edu.message;

/**
 * @ClassName PingMessage
 * @Author zlc
 * @Date 2021/10/9 下午4:32
 * @Description PingMessage
 * @Version 1.0
 */
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
