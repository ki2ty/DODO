package com.netty.edu.protocol;

import com.netty.edu.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TestMessageCodec
 * @Author zlc
 * @Date 2021/10/1 上午1:41
 * @Description TestMessageCodec
 * @Version 1.0
 */
@Slf4j
public class TestMessageCodec {

    public static void main(String[] args) throws Exception {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        LengthFieldBasedFrameDecoder frameDecoder = new LengthFieldBasedFrameDecoder(4096, 20, 4, 8, 0);
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                loggingHandler,
                frameDecoder,
                new MessageCodec());


        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zlc", "123qwe", "zzz");

        //  encode
//        embeddedChannel.writeOutbound(loginRequestMessage);

        //  decode
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, loginRequestMessage, byteBuf);
//        embeddedChannel.writeInbound(byteBuf);


        ByteBuf slice = byteBuf.slice(0, 100);
        byteBuf.retain();
        ByteBuf slice1 = byteBuf.slice(100, byteBuf.readableBytes() - 100);

        embeddedChannel.writeInbound(slice);
        Thread.sleep(5000);
        embeddedChannel.writeInbound(slice1);


    }


}
