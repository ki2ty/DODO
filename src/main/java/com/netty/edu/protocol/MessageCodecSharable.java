package com.netty.edu.protocol;

import com.netty.edu.conf.Config;
import com.netty.edu.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName MessageCodecSharable
 * @Author zlc
 * @Date 2021/10/1 上午2:44
 * @Description MessageCodecSharable
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, List<Object> list) throws Exception {
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer();
        //  1.4字节的魔数
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});

        //  2.4字节的版本
        byteBuf.writeInt(1);

        //  3.4字节的序列化方式      jdk 0   json 1
        byteBuf.writeInt(Config.getSerializerAlgorithms().ordinal());

        //  4.4字节的指令类型
        byteBuf.writeInt(message.getMessageType());

        //  5.4字节的请求序号
        byteBuf.writeInt(message.getSequenceId());

        //  6.获取正文长度
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(bos);
//        oos.writeObject(message);
//        byte[] bytes = bos.toByteArray();
        byte[] bytes = Config.getSerializerAlgorithms().serializer(message);


        //  7.4字节正文长度
        byteBuf.writeInt(bytes.length);

        //  填充到32字节
        byteBuf.writeBytes(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});

        //  8.正文
        byteBuf.writeBytes(bytes);

        log.debug("{} finish encode: {}", this.getClass().getName(), message.toString());
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //魔数
        int magicNum = byteBuf.readInt();
        //版本
        int version = byteBuf.readInt();
        //序列化方式
        int serializerType = byteBuf.readInt();
        //消息类型
        int messageType = byteBuf.readInt();
        //请求序号
        int requestSeq = byteBuf.readInt();
        //正文长度
        int length = byteBuf.readInt();
        //读空字节
        ByteBuf skip = byteBuf.readBytes(8);
        //读正文
        byte[] content = new byte[length];
        byteBuf.readBytes(content, 0, length);
        //jdk的序列化
//        ByteArrayInputStream bis = new ByteArrayInputStream(content);
//        ObjectInputStream ois = new ObjectInputStream(bis);
        //算法
        Serializer.Algorithms algorithms = Serializer.Algorithms.values()[serializerType];
        //确定消息类型
        Class<?> messageClass = Message.getMessageClass(messageType);
        Object message = algorithms.deserializer(messageClass, content);
        log.debug("finish decode: {},{},{},{},{},{}", magicNum, version, serializerType, messageType, requestSeq, length);
        log.debug("message: {}", message.toString());
        list.add(message);

    }
}
