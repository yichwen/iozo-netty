package io.iozo.net.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;

public class ShortByteLengthIncludeLengthEncoder extends LengthFieldPrepender {

    public ShortByteLengthIncludeLengthEncoder(int lengthFieldLength) {
        super(lengthFieldLength, true);
    }
}
