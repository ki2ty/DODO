package com.netty.edu.day3;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @ClassName TestNettyPromise
 * @Author zlc
 * @Date 2021/9/23 上午10:42
 * @Description TestNettyPromise
 * @Version 1.0
 */
@Slf4j
public class TestNettyPromise {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        DefaultPromise<Integer> promise = new DefaultPromise<>(new NioEventLoopGroup().next());

        new Thread(() -> {
            log.info("start");
            try {
                int i = 1 / 0;
                Thread.sleep(5000);
                promise.setSuccess(50);
            } catch (InterruptedException e) {
                promise.setFailure(e);
            }
        }).start();

        log.info("wait for result");
        log.info("result: {}", promise.get());


    }

}
