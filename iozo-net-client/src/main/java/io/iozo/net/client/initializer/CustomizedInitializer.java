package io.iozo.net.client.initializer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class CustomizedInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelHandler[] handlers;

    public CustomizedInitializer(ChannelHandler[] handlers) {
        this.handlers = handlers;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        for(ChannelHandler handler: handlers) {
            pipeline.addLast(handler);
        }
    }
}
