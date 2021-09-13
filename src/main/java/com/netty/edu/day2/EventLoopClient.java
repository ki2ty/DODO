package com.netty.edu.day2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @ClassName EventLoopClient
 * @Author zlc
 * @Date 2021/9/13 下午8:25
 * @Description EventLoopClient
 * @Version 1.0
 */
@Slf4j
public class EventLoopClient {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        ChannelFuture f = b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new LoggingHandler(LogLevel.DEBUG));
                        p.addLast(new StringEncoder());

                    }
                })
                //connect异步非阻塞
                //connect由主线程发起，由group中的线程执行，主线程不阻塞
                .connect(new InetSocketAddress(InetAddress.getLocalHost(), 8081));
        //添加listener
//        f.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                channelFuture.channel().writeAndFlush("123");
//                log.info(""+channelFuture);
//            }
//        });

        Channel channel = f.sync().channel();

        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.info("channel is closed");
            }
        });

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String next = scanner.next();
                if("/q".equals(next)){
                    channel.close();

                    break;
                }
                channel.writeAndFlush(next);
            }
        }, "thread-input").start();



//        f
//                .sync()
//                .channel()
//                .writeAndFlush("hi");
    }


}
