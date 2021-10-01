package com.netty.edu.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 * @ClassName GroupMembersResponseMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:18
 * @Description GroupMembersResponseMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
public class GroupMembersResponseMessage extends AbstractResponseMessage {

    private Set<String> members;

    public GroupMembersResponseMessage(Set<String> members) {
        this.members = members;
    }

    public GroupMembersResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }
}
