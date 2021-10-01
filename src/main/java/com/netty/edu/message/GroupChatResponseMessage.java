package com.netty.edu.message;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName GroupChatResponseMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:09
 * @Description GroupChatResponseMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
public class GroupChatResponseMessage extends AbstractResponseMessage {

    private String from;
    private String content;

    public GroupChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public GroupChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
    }
}
