package com.netty.edu.message;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName LoginResponseMessage
 * @Author zlc
 * @Date 2021/10/1 下午11:54
 * @Description LoginResponseMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {

    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }
}
