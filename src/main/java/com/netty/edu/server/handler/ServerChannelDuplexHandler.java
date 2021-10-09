package com.netty.edu.server.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ServerChannelDuplexHandler
 * @Author zlc
 * @Date 2021/10/9 下午4:46
 * @Description ServerChannelDuplexHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerChannelDuplexHandler extends ChannelDuplexHandler {
    //处理特殊事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.READER_IDLE) {
            log.info("读空闲时间已超过5s");
            ctx.channel().close();
        }
    }
}
