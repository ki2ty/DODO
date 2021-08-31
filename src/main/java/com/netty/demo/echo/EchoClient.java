package com.netty.demo.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * @ClassName EchoClient
 * @Author zlc
 * @Date 2021/8/31 下午4:07
 * @Description EchoClient
 * @Version 1.0
 */
public class EchoClient {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final String HOST = System.getProperty("host","127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port","8007"));
    static final int SIZE = Integer.parseInt(System.getProperty("size","256"));

    public static void main(String[] args) throws Exception{
        final SslContext sslContext;
        if(SSL){
            sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }else {
            sslContext = null;
        }

        //  Configure the client
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            if(sslContext != null){
                                p.addLast(sslContext.newHandler(socketChannel.alloc(),HOST,PORT));
                            }
                            p.addLast(new EchoClientHandler());
                        }
                    });

            //  Start client
            ChannelFuture f = b.connect(HOST, PORT).sync();

            //  Wait until the connection is close
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }

    }





}
