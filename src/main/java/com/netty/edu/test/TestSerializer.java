package com.netty.edu.test;

import com.netty.edu.conf.Config;
import com.netty.edu.message.LoginRequestMessage;
import com.netty.edu.message.LoginResponseMessage;
import com.netty.edu.message.Message;
import com.netty.edu.protocol.MessageCodecSharable;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TestSerializer
 * @Author zlc
 * @Date 2021/10/9 下午7:19
 * @Description TestSerializer
 * @Version 1.0
 */
@Slf4j
public class TestSerializer {

    public static void main(String[] args) {
        MessageCodecSharable messageCodecSharable = new MessageCodecSharable();
        LoggingHandler loggingHandler = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(loggingHandler, messageCodecSharable, loggingHandler);

//        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zlc", "123qwe", "zzz");
//        channel.writeOutbound(loginRequestMessage);

        channel.writeInbound(getBytes(new LoginResponseMessage(false, "failed")));


    }


    public static ByteBuf getBytes(Message message) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});
        byteBuf.writeInt(1);
        byteBuf.writeInt(Config.getSerializerAlgorithms().ordinal());
        byteBuf.writeInt(message.getMessageType());
        byteBuf.writeInt(message.getSequenceId());
        byte[] bytes = Config.getSerializerAlgorithms().serializer(message);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }
}
