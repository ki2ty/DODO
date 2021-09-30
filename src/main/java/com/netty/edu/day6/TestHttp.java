package com.netty.edu.day6;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * @ClassName TestHttp
 * @Author zlc
 * @Date 2021/9/27 下午6:07
 * @Description TestHttp
 * @Version 1.0
 */
@Slf4j
public class TestHttp {

    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, worker);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();
                    p.addLast(new LoggingHandler(LogLevel.DEBUG));
                    p.addLast(new HttpServerCodec());
                    p.addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
                            log.info("uri: {}", httpRequest.uri());

                            DefaultFullHttpResponse response =
                                    new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
                            byte[] bytes = "<h1>hello world</h1>".getBytes();
                            response.headers().addInt(CONTENT_LENGTH, bytes.length);
                            response.content().writeBytes(bytes);

                            channelHandlerContext.writeAndFlush(response);

                        }
                    });
//                    p.addLast(new ChannelInboundHandlerAdapter() {
//                        @Override
//                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                            log.info("{}", msg.getClass());
//
//                            if(msg instanceof HttpRequest){
//
//                            }
//
//                            if(msg instanceof HttpContent){
//
//                            }
//
//                        }
//                    });
                }
            });
            ChannelFuture f = b.bind(8081).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }


}
