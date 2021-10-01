package com.netty.edu.client;

import com.netty.edu.message.*;
import com.netty.edu.protocol.MessageCodecSharable;
import com.netty.edu.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName ChatClient
 * @Author zlc
 * @Date 2021/10/1 下午11:23
 * @Description ChatClient
 * @Version 1.0
 */
@Slf4j
public class ChatClient {


    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.INFO);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        CountDownLatch LATCH = new CountDownLatch(1);
        AtomicBoolean LOGIN = new AtomicBoolean(false);
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(LOGGING_HANDLER);
                            p.addLast(new ProtocolFrameDecoder());
                            p.addLast(messageCodec);
                            //建立连接后
                            p.addLast(new ChannelInboundHandlerAdapter() {
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
                                    log.info("msg: {}", msg);
                                    if (msg instanceof LoginResponseMessage) {
                                        LoginResponseMessage lrm = (LoginResponseMessage) msg;
                                        if (lrm.isSuccess()) {
                                            LOGIN.set(true);
                                        }
                                        LATCH.countDown();
                                    }
                                }
                            });
                        }

                    });
            ChannelFuture f = b.connect("127.0.0.1", 8081).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
        } finally {
            group.shutdownGracefully();
        }

    }


}
