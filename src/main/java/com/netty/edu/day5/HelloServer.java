package com.netty.edu.day5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HelloServer
 * @Author zlc
 * @Date 2021/9/25 下午4:09
 * @Description HelloServer
 * @Version 1.0
 */
@Slf4j
public class HelloServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            ChannelPipeline p = channel.pipeline();
                            //通过/n或者/r/n来分割消息
                            p.addLast(new LineBasedFrameDecoder(1024));


                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                        }
                    });
            ChannelFuture f = b.bind(8081).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("error: {}", e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }

}
