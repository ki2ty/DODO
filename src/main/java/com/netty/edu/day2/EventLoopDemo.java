package com.netty.edu.day2;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName EventLoopDemo
 * @Author zlc
 * @Date 2021/9/8 下午9:05
 * @Description EventLoopDemo
 * @Version 1.0
 */
@Slf4j
public class EventLoopDemo {

    public static void main(String[] args) {

        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();             // io时间，普通任务，定时任务
        EventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();     // 普通任务，定时任务

        //获取下一个事件循环对象
        EventLoop next = nioEventLoopGroup.next();


        //执行一个普通任务
//        next.submit(() -> {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            log.info("ok");
//        });

        //定时任务
        next.scheduleAtFixedRate(() -> log.info("ok"), 1, 1, TimeUnit.SECONDS);


        System.out.println(NettyRuntime.availableProcessors());


    }
}
