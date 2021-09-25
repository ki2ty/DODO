package com.netty.edu.day3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @ClassName TestJdkFuture
 * @Author zlc
 * @Date 2021/9/23 上午10:27
 * @Description TestJdkFuture
 * @Version 1.0
 */
@Slf4j
public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        Future<Integer> future = threadPool.submit(() -> {
            Thread.sleep(5000);
            return 50;
        });

        log.info("{}", future.get());
    }
}
