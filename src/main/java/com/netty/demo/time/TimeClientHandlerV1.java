package com.netty.demo.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @ClassName TimeServerHandlerV1
 * @Author zlc
 * @Date 2021/8/31 下午7:01
 * @Description TimeServerHandlerV1
 * @Version 1.0
 */
@Slf4j
public class TimeClientHandlerV1 extends ChannelInboundHandlerAdapter {

    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // define a ByteBuf to cumulate received data
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buf.release();
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf b = (ByteBuf) msg;
        buf.writeBytes(b);
        b.release();
        log.info("invoke channelRead");
        if (buf.readableBytes() >= 4) {
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ;
        ctx.close();
    }
}
