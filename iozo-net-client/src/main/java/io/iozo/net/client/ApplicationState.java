package io.iozo.net.client;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ApplicationState {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationState.class);

    private Channel channel;

    public void setChannel(Channel channel) {
        this.channel = channel;
        //logger.info("channel {} is set", channel.id().asLongText());
    }

    public void unsetChannel() {
        String id = this.channel.id().asLongText();
        this.channel = null;
        //logger.info("channel {} is unset", id);
    }

    public Channel getChannel() {
        return this.channel;
    }
}
