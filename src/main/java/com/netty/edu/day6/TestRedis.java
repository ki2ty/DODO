package com.netty.edu.day6;


import com.sun.javafx.util.Logging;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName TestRedis
 * @Author zlc
 * @Date 2021/9/26 下午4:05
 * @Description TestRedis
 * @Version 1.0
 */
@Slf4j
public class TestRedis {

    /*

    set name zhangsan

    *3
    $3
    set
    $4
    name
    $8
    zhangsan

     */

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline p = socketChannel.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.DEBUG));
                            p.addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ByteBuf buf = ctx.alloc().buffer();
                                    buf.writeBytes("*3\r\n".getBytes());
                                    buf.writeBytes("$3\r\n".getBytes());
                                    buf.writeBytes("set\r\n".getBytes());
                                    buf.writeBytes("$4\r\n".getBytes());
                                    buf.writeBytes("key-01\r\n".getBytes());
                                    buf.writeBytes("$8\r\n".getBytes());
                                    buf.writeBytes("aaa\r\n".getBytes());
                                    ctx.writeAndFlush(buf);
                                    log.info("finished");
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf b = (ByteBuf) msg;
                                    log.info(b.toString(CharsetUtil.UTF_8));
                                }
                            });

                        }
                    });
            ChannelFuture f = b.connect("127.0.0.1", 6379).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }


}
