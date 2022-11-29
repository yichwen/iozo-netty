package io.iozo.net.server.handler;

import io.iozo.net.server.state.ApplicationState;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(DefaultQueueListener.class);

    @Value("${server.input.queue}")
    private String queue;
    @Autowired
    private ApplicationState applicationState;

    @JmsListener(destination = "${server.input.queue}", containerFactory = "connectionFactory")
    private void onMessage(String msg, @Headers Map<String, Object> headers, MessageHeaders messageHeaders) {

        logger.info("*IN {} [{}]", "jms://" + queue, msg);

        String channelUniqueId = messageHeaders.get("jms_correlationId", String.class);
        if(channelUniqueId == null) {
            channelUniqueId = messageHeaders.get("TerminalID", String.class);
            if(channelUniqueId == null) {
                logger.error("JMSCorrelationID or TerminalID is missing in the response message. message: [{}]", msg);
                return;
            }
        }

        Channel channel;
        try {
            channel = applicationState.getChannelByUniqueId(channelUniqueId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(channel == null) {
            logger.error("channel unique id {} is not found in application state", channelUniqueId);
            return;
        }

        channel.writeAndFlush(msg);
        logger.info("*OUT {} [{}]", channel.remoteAddress(), msg);
    }
}
