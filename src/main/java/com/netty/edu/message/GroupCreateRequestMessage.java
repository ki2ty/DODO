package com.netty.edu.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

/**
 * @ClassName GroupCreateRequestMessage
 * @Author zlc
 * @Date 2021/10/2 上午1:12
 * @Description GroupCreateRequestMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreateRequestMessage extends Message {

    private String creator;
    private String groupName;
    private Set<String> members;


    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}
