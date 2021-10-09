package com.netty.edu.message;

/**
 * @ClassName PongMessage
 * @Author zlc
 * @Date 2021/10/9 下午4:34
 * @Description PongMessage
 * @Version 1.0
 */
public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
