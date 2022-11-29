package io.iozo.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

@Component
public class ApplicationBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationBootstrap.class);

    @Autowired
    private String processId;
    @Autowired
    private EventLoopGroup workerGroup;
    @Autowired
    private SocketAddress socketAddress;
    @Autowired
    private Bootstrap bootstrap;
    @Autowired
    private ChannelFutureListener connectionListener;
    @Autowired
    private ApplicationState applicationState;
    @Autowired
    @Qualifier("reconnectTime")
    private int reconnectTime;

    private ChannelFuture channelFuture;

    @PostConstruct
    private void connect() {
        logger.info("process {} is starting", processId);
        logger.info("client is connecting to {}", socketAddress);
        channelFuture = bootstrap.connect();
        channelFuture.addListener((ChannelFuture future) -> {
                if(!future.isSuccess()) {
                    logger.warn("client couldn't connect to " + socketAddress);
                    reconnect(workerGroup);
                } else {
                    Channel channel = future.channel();
                    logger.info("channel id \"{}\": remote address \"{}\" is connected",
                            channel.id().asLongText(), channel.remoteAddress());
                    applicationState.setChannel(channel);
                    channelFuture.channel().closeFuture().addListener(connectionListener);
                }
            });
    }

    public void reconnect(EventLoopGroup loop) {
        logger.info("client reconnect to {} in {} seconds", socketAddress, reconnectTime);
        loop.schedule(() -> { connect(); }, reconnectTime, TimeUnit.SECONDS);
    }

    public EventLoopGroup workerGroup() {
        return workerGroup;
    }

    public SocketAddress socketAddress() {
        return socketAddress;
    }

    public String processId() {
        return processId;
    }
}
