package io.iozo.net.server.state;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;

public class ChannelState {

    private Channel channel;
    private AtomicInteger index = new AtomicInteger(0);

    public ChannelState(Channel channel) {
        channel(channel);
    }

    public Channel channel() {
        return channel;
    }

    public void channel(Channel channel) {
        this.channel = channel;
    }

    public String getNextUniqueId() {
        return channel.id().asLongText() + "-" + index.incrementAndGet();
    }
}
