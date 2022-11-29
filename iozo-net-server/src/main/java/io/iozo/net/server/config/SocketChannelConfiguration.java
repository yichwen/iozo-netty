package io.iozo.net.server.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;

public final class SocketChannelConfiguration {

    @Value("${client.config.so-keep-alive:true}")
    private static boolean soKeepAlive;

    @Value("${client.config.so-linger:0}")
    private static int soLinger;

    @Value("${client.config.tcp-no-delay:true}")
    private static boolean soTcpNoDelay;

    public static ServerBootstrap configure(ServerBootstrap serverBootstrap) {
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, soKeepAlive)
                .childOption(ChannelOption.SO_LINGER, soLinger)
                .childOption(ChannelOption.TCP_NODELAY, soTcpNoDelay);
        return serverBootstrap;
    }
}
