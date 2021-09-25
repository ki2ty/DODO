package com.netty.edu.day3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TestPipeline
 * @Author zlc
 * @Date 2021/9/23 上午10:57
 * @Description TestPipeline
 * @Version 1.0
 */
@Slf4j
public class TestPipeline {

    public static void main(String[] args) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioServerSocketChannel) throws Exception {
                        ChannelPipeline p = nioServerSocketChannel.pipeline();
                        //netty会添加一个head & tail (handler)
                        p.addLast("h1",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("h1");
                                super.channelRead(ctx, msg);
                            }
                        });
                        p.addLast("h2",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("h2");
                                super.channelRead(ctx, msg);
                            }
                        });
                        p.addLast("h3",new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info("h3");
                                ctx.channel().writeAndFlush(ctx.alloc().buffer().writeBytes("hi".getBytes()));
                            }
                        });
                        p.addLast("h4", new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("h4");
                                super.write(ctx, msg, promise);
                            }
                        });
                        p.addLast("h5", new ChannelOutboundHandlerAdapter(){
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.info("h5");
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                })
                .bind(8081);
    }

}
