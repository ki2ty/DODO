package com.netty.demo.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName DiscardServerHandler
 * @Author zlc
 * @Date 2021/8/30 下午6:45
 * @Description DiscardServerHandler
 * @Version 1.0
 */

@Slf4j
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        try {
            //Do something with msg
//            while (byteBuf.isReadable()) {  //  (1) can be simplified to System.out.println(in.toString(CharsetUtil.UTF_8))
//                System.out.println((char) byteBuf.readByte());
//                System.out.flush();
//            }
            System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
        } finally {
            ReferenceCountUtil.release(msg);    //  (2)
//            byteBuf.release();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
