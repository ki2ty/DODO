package com.netty.edu.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName GroupQuitRequestMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:19
 * @Description GroupQuitRequestMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupQuitRequestMessage extends Message {

    private String userName;
    private String groupName;

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}
