package io.iozo.net.client.initializer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.nio.charset.Charset;

public class VariableFrameLengthInitializer extends ChannelInitializer<SocketChannel> {

    private ChannelHandler[] handlers;
    private int maxFrameLength = 65535;
    private int lengthFieldLength;
    private int lengthAdjustment;
    private boolean lengthIncludesLengthFieldLength;

    public VariableFrameLengthInitializer(ChannelHandler[] handlers, int lengthFieldLength) {
        this(handlers, lengthFieldLength, false);
    }

    public VariableFrameLengthInitializer(ChannelHandler[] handlers, int lengthFieldLength, boolean lengthIncludesLengthFieldLength) {
        this.handlers = handlers;
        this.lengthFieldLength = lengthFieldLength;
        this.lengthIncludesLengthFieldLength = lengthIncludesLengthFieldLength;
        this.lengthAdjustment = lengthIncludesLengthFieldLength ? lengthFieldLength * -1 : 0;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                maxFrameLength,
                0,
                lengthFieldLength,
                lengthAdjustment,
                0)
        );
        pipeline.addLast(new LengthFieldPrepender(lengthFieldLength, lengthIncludesLengthFieldLength));
        pipeline.addLast(new StringDecoder(Charset.defaultCharset()));
        pipeline.addLast(new StringEncoder(Charset.defaultCharset()));
        for(ChannelHandler handler: handlers) {
            pipeline.addLast(handler);
        }
    }
}
