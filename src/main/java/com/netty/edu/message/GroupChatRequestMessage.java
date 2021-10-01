package com.netty.edu.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName GroupChatRequestMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:08
 * @Description GroupChatRequestMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatRequestMessage extends Message {

    private String groupName;
    private String from;
    private String content;

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}
