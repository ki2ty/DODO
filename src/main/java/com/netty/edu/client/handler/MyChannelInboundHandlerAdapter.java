package com.netty.edu.client.handler;

import com.netty.edu.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName MyChannelInboundHandlerAdapter
 * @Author zlc
 * @Date 2021/10/9 下午4:44
 * @Description MyChannelInboundHandlerAdapter
 * @Version 1.0
 */
@Slf4j
public class MyChannelInboundHandlerAdapter extends ChannelInboundHandlerAdapter {
    private final CountDownLatch LATCH;
    private final AtomicBoolean LOGIN;

    public MyChannelInboundHandlerAdapter(CountDownLatch LATCH, AtomicBoolean LOGIN) {
        this.LATCH = LATCH;
        this.LOGIN = LOGIN;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入用户名");
            String userName = scanner.nextLine();
            System.out.println("请输入密码");
            String password = scanner.nextLine();
            LoginRequestMessage loginRequestMessage = new LoginRequestMessage(userName, password, "");
            ctx.writeAndFlush(loginRequestMessage);
            System.out.println("等待后续操作");
            try {
                LATCH.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!LOGIN.get()) {
                ctx.channel().close();
                return;
            }
            while (true) {
                System.out.println("==========================");
                System.out.println("send [user] [content]");
                System.out.println("g-send [group-name] [content]");
                System.out.println("g-create [group-name] [m1,m2,m3....]");
                System.out.println("g-members [group-name]");
                System.out.println("g-join [group-name]");
                System.out.println("g-quit [group-name]");
                System.out.println("quit");
                System.out.println("==========================");
                String op = scanner.nextLine();
                String[] s = op.split(" ");
                switch (s[0]) {
                    case "send":
                        ctx.writeAndFlush(new ChatRequestMessage(userName, s[1], s[2]));
                        break;
                    case "g-send":
                        ctx.writeAndFlush(new GroupChatRequestMessage(s[1], userName, s[2]));
                        break;
                    case "g-create":
                        String[] split = s[2].split(",");
                        Set<String> members = new HashSet<>(Arrays.asList(split));
                        ctx.writeAndFlush(new GroupCreateRequestMessage(userName, s[1], members));
                        break;
                    case "g-members":
                        ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                        break;
                    case "g-join":
                        ctx.writeAndFlush(new GroupJoinRequestMessage(userName, s[1]));
                        break;
                    case "g-quit":
                        ctx.writeAndFlush(new GroupQuitRequestMessage(userName, s[1]));
                        break;
                    case "quit":
                        ctx.channel().close();
                        return;

                }
            }

        }, "thread-login").start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.warn("msg: {}", msg);
        if (msg instanceof LoginResponseMessage) {
            LoginResponseMessage lrm = (LoginResponseMessage) msg;
            if (lrm.isSuccess()) {
                LOGIN.set(true);
            }
            LATCH.countDown();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("connection is closed");
        LATCH.countDown();
        log.debug("press any key to continue");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
