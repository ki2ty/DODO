package com.netty.demo.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @ClassName TimeClient
 * @Author zlc
 * @Date 2021/8/31 下午5:57
 * @Description TimeClient
 * @Version 1.0
 */
public class TimeClient {


    static final int PORT = Integer.parseInt(System.getProperty("port", "8008"));
    static final String HOST = System.getProperty("host", "127.0.0.1");


    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.channel(NioSocketChannel.class)
                    .group(group)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new TimeClientHandlerV1());
                        }
                    });
//            .handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                protected void initChannel(SocketChannel socketChannel) throws Exception {
//                    ChannelPipeline p = socketChannel.pipeline();
//                    p.addLast(new TimeDecoder(),new TimeClientHandler());
//                }
//            });
            ChannelFuture f = b.connect(HOST, PORT).sync();

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }


    }


}
