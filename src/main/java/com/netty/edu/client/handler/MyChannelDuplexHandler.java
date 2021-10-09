package com.netty.edu.client.handler;

import com.netty.edu.message.PingMessage;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName MyChannelDuplexHandler
 * @Author zlc
 * @Date 2021/10/9 下午4:42
 * @Description MyChannelDuplexHandler
 * @Version 1.0
 */
@Slf4j
public class MyChannelDuplexHandler extends ChannelDuplexHandler {
    //处理特殊事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.WRITER_IDLE) {
            log.debug("写空闲时间已超过x秒");
            //发送心跳包
            ctx.writeAndFlush(new PingMessage());
        }else if(event.state() == IdleState.READER_IDLE){
            log.info("读空闲超过x秒，与客户端失去连接");
        }
    }
}
