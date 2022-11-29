package io.iozo.net.server.state;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApplicationState {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationState.class);

    private Map<String, ChannelState> channels = new ConcurrentHashMap<>();

    public void channelActive(Channel channel) {
        String channelId = channel.id().asLongText();
        ChannelState placeholder = channels.putIfAbsent(channelId, new ChannelState(channel));
        if(placeholder != null) {
            logger.warn("channel id {} had been added to application state", channelId);
        } else {
            logger.info("channel id {} is added to application state", channelId);
        }
    }

    public void channelInactive(Channel channel) {
        String channelId = channel.id().asLongText();
        ChannelState placeholder = channels.remove(channel.id().asLongText());
        if(placeholder == null) {
            logger.warn("channel id {} had been removed from application state", channelId);
        } else {
            logger.info("channel id {} is removed from application state", channelId);
        }
    }

    private ChannelState getChannelState(String channelId) {
        ChannelState channelState = channels.get(channelId);
        if(channelState == null) {
            throw new IllegalStateException("channel id \"" + channelId + "\" is not found");
        }
        return channelState;
    }

    public Channel getChannelByUniqueId(String uniqueId) {
//        int index = uniqueId.lastIndexOf("-");
//        String channelId;
//        if(index < 0) {
//            channelId = uniqueId;
//        } else {
//            channelId = uniqueId.substring(0, index);
//        }
        return getChannelState(uniqueId).channel();
    }

    public String getChannelNextUniqueId(String channelId) {
        return getChannelState(channelId).getNextUniqueId();
    }
}
