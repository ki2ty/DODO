package com.netty.demo.discard;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName DiscardClient
 * @Author zlc
 * @Date 2021/8/31 下午2:37
 * @Description DiscardClient
 * @Version 1.0
 */
@Slf4j
public class DiscardClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8082));

        socketChannel.write(StandardCharsets.UTF_8.encode("Shit bro ! 你故意找茬是不是"));

        log.info("ok");



    }
}
