package com.netty.edu.day5;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HelloClient
 * @Author zlc
 * @Date 2021/9/25 下午4:09
 * @Description HelloClient
 * @Version 1.0
 */
@Slf4j
public class HelloClient {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline p = channel.pipeline();
                            p.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    for (int i = 0; i < 10; i++) {
                                        ByteBuf buffer = ctx.alloc().buffer();
                                        buffer.writeBytes("hello world\n".getBytes());
                                        ctx.writeAndFlush(buffer);
                                    }
                                }
                            });
                        }
                    });
            ChannelFuture f = b.connect("127.0.0.1", 8081).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("error: {}", e.getMessage());
        } finally {
            group.shutdownGracefully();
        }

    }

}
