package com.netty.edu.day1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HelloClient
 * @Author zlc
 * @Date 2021/8/31 下午4:48
 * @Description HelloClient
 * @Version 1.0
 */
@Slf4j
public class HelloClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");

    static final int PORT = Integer.parseInt(System.getProperty("port", "8081"));


    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            Channel channel = b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new StringEncoder());
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .connect(HOST, PORT)
                    .sync().channel();

            log.info("stop here");
        } finally {
            group.shutdownGracefully();
        }

    }


}
