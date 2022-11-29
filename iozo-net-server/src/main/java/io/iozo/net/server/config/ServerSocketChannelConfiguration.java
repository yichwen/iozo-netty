package io.iozo.net.server.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;

public final class ServerSocketChannelConfiguration {

    @Value("${server.config.so-backlog:10}")
    private static int soBacklog;

    @Value("${server.config.so-reuse-addr:false}")
    private static boolean soReuseAddr;

    public static ServerBootstrap configure(ServerBootstrap serverBootstrap) {
        serverBootstrap.option(ChannelOption.SO_BACKLOG, soBacklog)
            .option(ChannelOption.SO_REUSEADDR, soReuseAddr);
        return serverBootstrap;
    }

}
