package com.netty.edu.client;

import com.netty.edu.client.handler.ClientIdleStateHandler;
import com.netty.edu.client.handler.MyChannelDuplexHandler;
import com.netty.edu.client.handler.MyChannelInboundHandlerAdapter;
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
                            p.addLast(new ProtocolFrameDecoder());
                            p.addLast(messageCodec);
                            p.addLast(LOGGING_HANDLER);
                            //判断读写空闲时间过长
                            p.addLast(new ClientIdleStateHandler());
                            //同时作为入栈和出栈处理器
                            p.addLast(new MyChannelDuplexHandler());
                            //建立连接后
                            p.addLast(new MyChannelInboundHandlerAdapter(LATCH, LOGIN));
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
