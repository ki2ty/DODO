package com.netty.edu.day4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;


import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @ClassName TestSlice
 * @Author zlc
 * @Date 2021/9/24 下午3:54
 * @Description TestSlice
 * @Version 1.0
 */
public class TestSlice {

    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        buffer.writeBytes("hello world".getBytes());
        log(buffer);

        ByteBuf slice = buffer.slice(0, 5);
        log(slice);
        ByteBuf slice1 = buffer.slice(5, 5);
        log(slice1);
    }

    public static void log(ByteBuf buf) {
        int length = buf.readableBytes();
        int row = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder sb = new StringBuilder(row * 80 * 2)
                .append("read index:").append(buf.readerIndex())
                .append(" write index:").append(buf.writerIndex())
                .append(" capacity:").append(buf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(sb, buf);
        System.out.println(sb.toString());
    }

}
