package com.netty.edu.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName LoginRequestMessage
 * @Author zlc
 * @Date 2021/10/1 上午1:42
 * @Description LoginRequestMessage
 * @Version 1.0
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestMessage extends Message {

    private String userName;
    private String password;
    private String nickName;


    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
