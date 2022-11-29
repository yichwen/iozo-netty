package io.iozo.net.client.handler;

import io.iozo.net.client.ApplicationState;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Sharable
public class DefaultChannelHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultChannelHandler.class);

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ApplicationState applicationState;
    @Value("${client.out.queue}")
    private String outputQueue;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        logger.info("*IN  {} [{}]", channel.remoteAddress(), msg);

        jmsTemplate.convertAndSend(outputQueue, msg);
        logger.info("*OUT {} [{}]", "jms://" + outputQueue, msg);
    }
}
