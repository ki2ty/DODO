package com.netty.edu.server;

import com.netty.edu.protocol.MessageCodecSharable;
import com.netty.edu.protocol.ProtocolFrameDecoder;
import com.netty.edu.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ChatServer
 * @Author zlc
 * @Date 2021/10/1 上午2:31
 * @Description ChatServer
 * @Version 1.0
 */
@Slf4j
public class ChatServer {

    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable messageCodec = new MessageCodecSharable();
        LoginRequestMessageHandler loginRequestMessageHandler = new LoginRequestMessageHandler();
        ChatRequestMessageHandler chatRequestMessageHandler = new ChatRequestMessageHandler();
        GroupChatRequestMessageHandler groupChatRequestMessageHandler = new GroupChatRequestMessageHandler();
        GroupCreateRequestMessageHandler groupCreateRequestMessageHandler = new GroupCreateRequestMessageHandler();
        GroupJoinRequestMessageHandler groupJoinRequestMessageHandler = new GroupJoinRequestMessageHandler();
        GroupMembersRequestMessageHandler groupMembersRequestMessageHandler = new GroupMembersRequestMessageHandler();
        GroupQuitRequestMessageHandler groupQuitRequestMessageHandler = new GroupQuitRequestMessageHandler();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(LOGGING_HANDLER);
                            p.addLast(new ProtocolFrameDecoder());
                            p.addLast(messageCodec);
                            p.addLast(loginRequestMessageHandler);
                            p.addLast(chatRequestMessageHandler);
                            p.addLast(groupChatRequestMessageHandler);
                            p.addLast(groupCreateRequestMessageHandler);
                            p.addLast(groupJoinRequestMessageHandler);
                            p.addLast(groupMembersRequestMessageHandler);
                            p.addLast(groupQuitRequestMessageHandler);
                        }
                    });
            ChannelFuture f = b.bind(8081).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }

}
