package com.netty.edu.message;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName GroupCreateResponseMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:14
 * @Description GroupCreateResponseMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
public class GroupCreateResponseMessage extends AbstractResponseMessage {

    private String creator;
    private String groupName;

    public GroupCreateResponseMessage(String creator, String groupName) {
        this.creator = creator;
        this.groupName = groupName;
    }

    public GroupCreateResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupCreateResponseMessage;
    }
}
