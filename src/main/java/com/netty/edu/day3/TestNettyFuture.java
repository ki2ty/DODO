package com.netty.edu.day3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @ClassName TestNettyFuture
 * @Author zlc
 * @Date 2021/9/23 上午10:34
 * @Description TestNettyFuture
 * @Version 1.0
 */
@Slf4j
public class TestNettyFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        EventLoop loop = group.next();

        Future<Integer> future = loop.submit(() -> {
            Thread.sleep(5000);
            return 50;
        });

        future.addListener(future1 -> {
           log.info("{}",future1.getNow());
        });

        group.shutdownGracefully();

    }

}
