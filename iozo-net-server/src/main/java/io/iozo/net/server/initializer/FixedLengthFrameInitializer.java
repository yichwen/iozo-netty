package io.iozo.net.server.initializer;

import io.iozo.net.server.handler.ChannelActivityHandler;
import io.iozo.net.server.handler.DefaultChannelHandler;
import io.iozo.net.server.handler.DefaultExceptionHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class FixedLengthFrameInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${decoder.fixed-length}")
    private int fixedLength;

    @Autowired
    private ChannelActivityHandler channelActivityHandler;
    @Autowired
    private DefaultChannelHandler defaultChannelHandler;
    @Autowired
    private DefaultExceptionHandler defaultExceptionHandler;

    @Override
    public void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();
        if(fixedLength <= 0) {
            String error = "fixed length \"" + fixedLength + "\" for decoder is invalid";
            throw new IllegalArgumentException(error);
        }
        pipeline.addLast(channelActivityHandler);
        pipeline.addLast(new FixedLengthFrameDecoder(fixedLength));
        pipeline.addLast(new StringDecoder(Charset.defaultCharset()));
        pipeline.addLast(new StringEncoder(Charset.defaultCharset()));
        pipeline.addLast(defaultChannelHandler);
        pipeline.addLast(defaultExceptionHandler);
    }
}
