package io.iozo.net.server.config;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

@Configuration
public class MQConfiguration {

    @Bean
    public ConnectionFactory mqConnectionFactory() {
        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
        factory.setHostName("localhost");
        try {
            factory.setPort(1414);
            factory.setQueueManager("DEVQM");
            factory.setChannel("DEV.MQ");
            factory.setTransportType(1);
            factory.setTargetClientMatching(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> connectionFactory(ConnectionFactory mqConnectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, mqConnectionFactory);
        return factory;
    }

}
