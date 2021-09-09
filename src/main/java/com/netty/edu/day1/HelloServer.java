package com.netty.edu.day1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HelloServer
 * @Author zlc
 * @Date 2021/8/30 下午6:27
 * @Description HelloServer
 * @Version 1.0
 */
@Slf4j
public class HelloServer {


    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ChannelFuture f = new ServerBootstrap()
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new StringDecoder())
                                    .addLast(new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            System.out.println(msg);
                                        }
                                    });
                        }
                    })
                    .bind(8081).sync();

            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }

    }

}
