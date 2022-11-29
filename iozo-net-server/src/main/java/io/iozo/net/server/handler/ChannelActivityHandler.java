package io.iozo.net.server.handler;

import io.iozo.net.server.state.ApplicationState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ChannelActivityHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ApplicationState applicationState;

    private final static Logger logger = LoggerFactory.getLogger(ChannelActivityHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        logger.info("channel id \"{}\": remote address \"{}\" is connected",
                channel.id().asLongText(), channel.remoteAddress());
        applicationState.channelActive(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        logger.info("channel id \"{}\": remote address \"{}\" is disconnected",
                channel.id().asLongText(), channel.remoteAddress());
        applicationState.channelInactive(channel);
    }
    
}
