package com.netty.edu.day5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @ClassName TestLengthFieldDecoder
 * @Author zlc
 * @Date 2021/9/25 下午5:41
 * @Description TestLengthFieldDecoder
 * @Version 1.0
 */
public class TestLengthFieldDecoder {

    public static void main(String[] args) {

        EmbeddedChannel channel = new EmbeddedChannel();
        ChannelPipeline p = channel.pipeline();
        p.addLast(new LengthFieldBasedFrameDecoder(1024,0,4,1,4));
        p.addLast(new LoggingHandler(LogLevel.DEBUG));


        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        for (int i = 0; i < 10; i++) {
            byte[] bytes = "hello world".getBytes();
            int length = bytes.length;
            buffer.writeInt(length)
                    .writeByte(1)
                    .writeBytes(bytes);
        }
        channel.writeInbound(buffer);



    }

}
