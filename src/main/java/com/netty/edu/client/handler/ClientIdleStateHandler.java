package com.netty.edu.client.handler;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @ClassName ClientIdleStateHandler
 * @Author zlc
 * @Date 2021/10/9 下午4:43
 * @Description ClientIdleStateHandler
 * @Version 1.0
 */
public class ClientIdleStateHandler extends IdleStateHandler {
    public ClientIdleStateHandler() {
        super(0, 0, 0);
    }
}
