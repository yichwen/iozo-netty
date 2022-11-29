package io.iozo.net.client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Arrays;

public class FixedLengthFrameEncoder extends MessageToByteEncoder<ByteBuf> {

    private final int frameLength;
    private final byte[] frameBytes;

    public FixedLengthFrameEncoder(int frameLength) {
        this(frameLength, (byte)' ');
    }

    public FixedLengthFrameEncoder(int frameLength, byte defaultByte) {
        this.frameLength = frameLength;
        this.frameBytes = new byte[frameLength];
        Arrays.fill(frameBytes, defaultByte);
    }

    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, ByteBuf in, ByteBuf out) throws Exception {
        int dataLength = in.readableBytes();
        if(dataLength > frameLength) {
            out.writeBytes(in, 0, frameLength);
        } else if(dataLength < frameLength) {
            in.getBytes(0, frameBytes, 0, dataLength);
            out.writeBytes(frameBytes);
        } else {
            out.writeBytes(in);
        }
    }
}
