package com.netty.edu.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName ChatRequestMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:00
 * @Description ChatRequestMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestMessage extends Message {

    private String from;
    private String to;
    private String content;

    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }
}
