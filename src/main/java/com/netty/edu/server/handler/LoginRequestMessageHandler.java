package com.netty.edu.server.handler;

import com.netty.edu.message.LoginRequestMessage;
import com.netty.edu.message.LoginResponseMessage;
import com.netty.edu.server.service.UserService;
import com.netty.edu.server.service.UserServiceFactory;
import com.netty.edu.server.session.Session;
import com.netty.edu.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName LoginRequestMessageHandler
 * @Author zlc
 * @Date 2021/10/2 上午1:42
 * @Description LoginRequestMessageHandler
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestMessage loginRequestMessage) throws Exception {
        String userName = loginRequestMessage.getUserName();
        String password = loginRequestMessage.getPassword();
        UserService service = UserServiceFactory.getService();
        boolean success = service.login(userName, password);
        if (success) {
            Session session = SessionFactory.getSession();
            session.bind(channelHandlerContext.channel(), userName);
        }
        LoginResponseMessage msg = new LoginResponseMessage(success, success ? "登陆成功" : "登录失败");
        channelHandlerContext.writeAndFlush(msg);
    }
}
