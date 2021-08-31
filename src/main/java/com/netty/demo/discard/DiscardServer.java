package com.netty.demo.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @ClassName DiscardServer
 * @Author zlc
 * @Date 2021/8/30 下午6:42
 * @Description discard any incoming data
 * @Version 1.0
 */
@Slf4j
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }


    /**
     *  (1)     NioEventLoopGroup是一个多线程的event loop用来进行I/O操作，bossGroup用来连接请求并将其注册到workersGroup
     *
     *  (2)     ServerBootstrap是一个用来创建一个server的帮助类，也可以直接使用Channel创建服务器，但那将是一个tedious的过程，在大多数情况下都不需要使用这种方式
     *
     *  (3)     NioServerSocketChannel用来接收服务器接收到的连接请求
     *
     *  (4)     应用逐渐复杂后，当你需要添加更多的handler到pipeline中时就需要将这个匿名类抽象为一个top-level class
     *
     *  (5)     配置bossGroup中的Channel
     *
     *  (6)     配置workerGroup中的Channel
     *
     *  (7)     bind可以绑定多次，可以绑定多个端口
     *
     */
    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();  //  (2)
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  //  (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { //  (4)
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)  //  (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //  (6)
            ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();   //  (7)
            bootstrap.bind(8082).sync();    //可以bind多个端口


            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8081;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).run();
    }


}
