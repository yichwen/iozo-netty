package io.iozo.net.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;

public class ShortByteLengthExcludeLengthEncoder extends LengthFieldPrepender {

    public ShortByteLengthExcludeLengthEncoder(int lengthFieldLength) {
        super(lengthFieldLength, false);
    }
}
