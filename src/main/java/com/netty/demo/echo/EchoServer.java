package com.netty.demo.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * @ClassName EchoServer
 * @Author zlc
 * @Date 2021/8/31 下午4:07
 * @Description Echo back any received data from client
 * @Version 1.0
 */
public class EchoServer {

    static final boolean SSL = System.getProperty("ssl") != null;

    static final int port = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        //  Configure SSL
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        //  Configure the server
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        final EchoServerHandler serverHandler = new EchoServerHandler();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(socketChannel.alloc()));
                            }
                            p.addLast(serverHandler);
                        }
                    });

            //  Start the server
            ChannelFuture f = b.bind(port).sync();

            //  Wait until the server socket is closed
            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
