package com.netty.edu.message;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName ChatResponseMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:03
 * @Description ChatResponseMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
public class ChatResponseMessage extends AbstractResponseMessage {

    private String from;
    private String content;

    public ChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public ChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }
}
