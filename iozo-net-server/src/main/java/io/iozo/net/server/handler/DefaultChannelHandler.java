package io.iozo.net.server.handler;

import io.iozo.net.server.state.ApplicationState;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
@ChannelHandler.Sharable
public class DefaultChannelHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultChannelHandler.class);

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ApplicationState applicationState;

    @Value("${server.output.queue}")
    private String outputQueue;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        logger.info("*IN  {} [{}]", channel.remoteAddress(), msg);

        jmsTemplate.convertAndSend(outputQueue, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                String id = applicationState.getChannelNextUniqueId(channel.id().asLongText());
                message.setJMSCorrelationID(id);
                return message;
            }
        });
        logger.info("*OUT {} [{}]", "jms://" + outputQueue, msg);
    }
}
