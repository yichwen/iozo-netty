package io.iozo.net.client.handler;

import io.iozo.net.client.ApplicationBootstrap;
import io.iozo.net.client.ApplicationState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultConnectionListener implements ChannelFutureListener {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConnectionListener.class);

    @Autowired
    private ApplicationBootstrap applicationBootstrap;
    @Autowired
    private ApplicationState applicationState;

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(future.isSuccess()) {
            Channel channel = future.channel();
            applicationBootstrap.reconnect(channel.eventLoop());
            channel.close();
            logger.info("channel id \"{}\": remote address \"{}\" is disconnected",
                    channel.id().asLongText(), channel.remoteAddress());
            applicationState.unsetChannel();
        } else {
            logger.warn(future.cause().getMessage());
        }
    }
    
}
