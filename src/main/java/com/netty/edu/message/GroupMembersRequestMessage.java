package com.netty.edu.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName GroupMembersRequestMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:17
 * @Description GroupMembersRequestMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembersRequestMessage extends Message {

    private String groupName;

    @Override
    public int getMessageType() {
        return GroupMembersRequestMessage;
    }
}
