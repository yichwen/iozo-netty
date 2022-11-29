package io.iozo.net.client.config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;

public final class SocketChannelConfig {

    @Value("${client.config.so-keep-alive:false}")
    private static boolean soKeepAlive;

    @Value("${client.config.so-linger:-1}")
    private static int soLinger;

    @Value("${client.config.tcp-no-delay:false}")
    private static boolean soTcpNoDelay;

    public static void configure(Bootstrap bootstrap) {
        bootstrap.option(ChannelOption.SO_KEEPALIVE, soKeepAlive)
                .option(ChannelOption.SO_LINGER, soLinger)
                .option(ChannelOption.TCP_NODELAY, soTcpNoDelay);
    }
}
