package io.iozo.net.client;

import io.iozo.net.client.config.SocketChannelConfig;
import io.iozo.net.client.handler.DefaultChannelHandler;
import io.iozo.net.client.handler.DefaultConnectionListener;
import io.iozo.net.client.handler.DefaultExceptionHandler;
import io.iozo.net.client.initializer.FixedLengthFrameInitializer;
import io.iozo.net.client.initializer.VariableFrameLengthInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.net.InetSocketAddress;

@Configuration
public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    @Autowired
    private DefaultChannelHandler defaultChannelHandler;
    @Autowired
    private DefaultExceptionHandler defaultExceptionHandler;

    @Value("${client.process-id}")
    private String processId;
    @Value("${client.reconnect-time:30}")
    private int reconnectTime;
    @Value("${client.connect-ip}")
    private String host;
    @Value("${client.connect-port}")
    private int port;
    @Value("${client.task-count:0}")
    private int taskCount;
    @Value("${client.initializer}")
    private String initializer;
    @Value("${client.fixed-frame-length:0}")
    private int fixedFrameLength;
    @Value("${client.byte-frame-length:0}")
    private int byteFrameLength;
    @Value("${client.include-length:false}")
    private boolean includeLength;

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    EventLoopGroup workerGroup() {
        if(taskCount == 0) {
            return new NioEventLoopGroup();
        }
        return new NioEventLoopGroup(taskCount);
    }

    @Bean(name = "socketAddress")
    InetSocketAddress socketAddress() {
        return new InetSocketAddress(host, port);
    }

    @Bean(name = "bootstrap")
    Bootstrap bootstrap() {
        Bootstrap b = new Bootstrap();
        b.group(workerGroup())
            .channel(NioSocketChannel.class)
            .remoteAddress(socketAddress())
            .handler(initializer());
        SocketChannelConfig.configure(b);
        return b;
    }

    @Bean(name = "initializer")
    ChannelInitializer<SocketChannel> initializer() {
        if(initializer.equalsIgnoreCase("fixedlengthframeinitializer")) {
            return new FixedLengthFrameInitializer(new ChannelHandler[]{
                    defaultChannelHandler,
                    defaultExceptionHandler
            }, fixedFrameLength);
        } else if(initializer.equalsIgnoreCase("variableframelengthinitializer")) {
            return new VariableFrameLengthInitializer(new ChannelHandler[]{
                    defaultChannelHandler,
                    defaultExceptionHandler
            }, byteFrameLength, includeLength);
        } else {
            return null;
        }
    }

    @Bean(name = "connectionListener")
    ChannelFutureListener connectionListener() {
        return new DefaultConnectionListener();
    }

    @Bean(name = "reconnectTime")
    int reconnectTime() {
        return reconnectTime;
    }

    @Bean(name = "processId")
    String processId() {
        return processId;
    }
}
