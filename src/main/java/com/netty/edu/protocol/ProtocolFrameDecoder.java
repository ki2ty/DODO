package com.netty.edu.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @ClassName ProtocolFrameDecoder
 * @Author zlc
 * @Date 2021/10/1 下午11:25
 * @Description ProtocolFrameDecoder
 * @Version 1.0
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFrameDecoder() {
        this(4096, 20, 4, 8, 0);
    }

    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
