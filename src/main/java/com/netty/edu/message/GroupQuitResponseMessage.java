package com.netty.edu.message;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName GroupQuitResponseMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:20
 * @Description GroupQuitResponseMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
public class GroupQuitResponseMessage extends AbstractResponseMessage {

    private String quitMember;

    public GroupQuitResponseMessage(String quitMember) {
        this.quitMember = quitMember;
    }

    public GroupQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupQuitResponseMessage;
    }
}
