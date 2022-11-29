package io.iozo.net.server.config;

import io.iozo.net.server.initializer.FixedLengthFrameInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Configuration
public class ServerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);

    @Autowired
    private ApplicationContext context;
    @Value("${server.boss-count:0}")
    private int bossCount;
    @Value("${server.worker-count:0}")
    private int workerCount;
    @Value("${server.tcp-port:19000}")
    private int port;

    @Autowired
    private FixedLengthFrameInitializer fixedLengthFrameInitializer;

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public EventLoopGroup workerGroup() {
        if(workerCount > 0) {
            return new NioEventLoopGroup(workerCount);
        }
        return new NioEventLoopGroup();
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public EventLoopGroup bossGroup() {
        if(bossCount > 0) {
            return new NioEventLoopGroup(bossCount);
        }
        return new NioEventLoopGroup();
    }

    @Bean(name = "socketAddress")
    public InetSocketAddress socketAddress() {
        return new InetSocketAddress(port);
    }

    public ServerBootstrap serverBootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
            .channel(NioServerSocketChannel.class)
            .localAddress(socketAddress());

        Object bean = context.getBean("fixedLengthFrameInitializer");
        if(bean instanceof ChannelInitializer) {
            b.childHandler((ChannelInitializer)bean);
        }
        ServerSocketChannelConfiguration.configure(b);
        SocketChannelConfiguration.configure(b);
        return b;
    }

    private ChannelFuture serverChannelFuture;

    @PostConstruct
    public void start() throws Exception {
        logger.info("Starting server at {}", socketAddress());
        ServerBootstrap b = serverBootstrap();
        serverChannelFuture = b.bind().sync();
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannelFuture.channel().closeFuture().sync();
    }
}
