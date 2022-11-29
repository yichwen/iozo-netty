package io.iozo.net.client.initializer;

import io.iozo.net.client.codec.FixedLengthFrameEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.nio.charset.Charset;

public class FixedLengthFrameInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelHandler[] handlers;
    private int frameLength;

    public FixedLengthFrameInitializer(ChannelHandler[] handlers, int frameLength) {
        this.handlers = handlers;
        this.frameLength = frameLength;
    }

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new FixedLengthFrameDecoder(frameLength));
        pipeline.addLast(new FixedLengthFrameEncoder(frameLength));
        pipeline.addLast(new StringDecoder(Charset.defaultCharset()));
        pipeline.addLast(new StringEncoder(Charset.defaultCharset()));
        for(ChannelHandler handler: handlers) {
            pipeline.addLast(handler);
        }
    }
}
