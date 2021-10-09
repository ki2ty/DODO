package com.netty.edu.server.handler;

import com.netty.edu.server.session.Session;
import com.netty.edu.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ClientQuitHandler
 * @Author zlc
 * @Date 2021/10/9 下午3:55
 * @Description ClientQuitHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientQuitHandler extends ChannelInboundHandlerAdapter {

    //连接正常断开时触发inactive事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = SessionFactory.getSession();
        String user = session.getUser(ctx.channel());
        if(user != null){
            session.unbind(ctx.channel());
            log.debug("【 {} 】用户已正常退出", user);
        }else{
            log.debug("未登录的客户端退出");
        }
    }


    //异常断开
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Session session = SessionFactory.getSession();
        String user = session.getUser(ctx.channel());
        if(user != null){
            session.unbind(ctx.channel());
            log.debug("【 {} 】用户异常断开", user);
        }else{
            log.debug("未登录的客户端异常退出");
        }
    }
}
