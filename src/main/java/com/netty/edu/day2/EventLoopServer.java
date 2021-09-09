package com.netty.edu.day2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @ClassName EventLoopServer
 * @Author zlc
 * @Date 2021/9/8 下午9:21
 * @Description EventLoopServer
 * @Version 1.0
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) throws InterruptedException {

        //创建一个独立的EventLoopGroup执行耗时操作
        EventLoopGroup worker1 = new DefaultEventLoopGroup();

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.info(byteBuf.toString(CharsetUtil.UTF_8));
                                //将消息传递给下一个handler
                                ctx.fireChannelRead(msg);
                            }
                        });
                        p.addLast(worker1, "worker1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.info(byteBuf.toString(CharsetUtil.UTF_8));
                            }
                        });
                    }
                });
        ChannelFuture f = b.bind(new InetSocketAddress(8081)).sync();

        f.channel().closeFuture().sync();


    }
}
