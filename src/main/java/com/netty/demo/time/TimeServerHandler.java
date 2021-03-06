package com.netty.demo.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TimeServerHandler
 * @Author zlc
 * @Date 2021/8/31 下午4:51
 * @Description TimeServerHandler
 * @Version 1.0
 */
@Slf4j
public class TimeServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf timeBuffer = ctx.alloc().buffer(4);
        timeBuffer.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        log.info("1. buffer refCnt: [{}]", timeBuffer.refCnt());
        timeBuffer.retain();
        log.info("3. buffer refCnt: [{}]", timeBuffer.refCnt());
        ChannelFuture f = ctx.writeAndFlush(timeBuffer);
        log.info("2. buffer refCnt: [{}]", timeBuffer.refCnt());
        //writeAndFlush   ->    any requested operation might not have been performed yet because all operations are asynchronous in Netty
        //the following code might close the connection even before a message is sent
        f.addListener((ChannelFutureListener) channelFuture -> {
            assert f == channelFuture;
            //  close() might not close the connection immediately, and it return a ChannelFuture
//            ctx.close();
        });
        Thread.sleep(500);
        timeBuffer.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        ChannelFuture f1 = ctx.writeAndFlush(timeBuffer);
        log.info("4. buffer refCnt: [{}]", timeBuffer.refCnt());
        f1.addListener((ChannelFutureListener) channelFuture -> {
            assert f1 == channelFuture;
            //  close() might not close the connection immediately, and it return a ChannelFuture
            ctx.close();
        });


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
