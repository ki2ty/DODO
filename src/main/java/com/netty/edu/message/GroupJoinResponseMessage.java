package com.netty.edu.message;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName GroupJoinResponseMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:16
 * @Description GroupJoinResponseMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
public class GroupJoinResponseMessage extends AbstractResponseMessage {

    private String joinMember;

    public GroupJoinResponseMessage(String joinMember) {
        this.joinMember = joinMember;
    }

    public GroupJoinResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupJoinResponseMessage;
    }
}
