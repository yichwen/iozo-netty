package io.iozo.net.client.handler;

import io.iozo.net.client.ApplicationState;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class DefaultQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(DefaultQueueListener.class);

    @Autowired
    private ApplicationState applicationState;

    @Value("${client.queue}")
    private String queue;

    @JmsListener(destination = "${client.queue}")
    private void onMessage(String msg, @Headers Map headers) {

        logger.info("*IN {} [{}]", "jms://" + queue, msg);

        Channel channel = applicationState.getChannel();
        if(channel == null) {
            logger.error("no available channel");
            return;
        }

        channel.writeAndFlush(msg);
        logger.info("*OUT {} [{}]", channel.remoteAddress(), msg);
    }
}
