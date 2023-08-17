package com.kepler.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfiguration {
    @Value("${rabbitmq.hostname}")
    private String hostname;
    @Value("${rabbitmq.username}")
    private String username;
    @Value("${rabbitmq.password}")
    private String password;
    @Value("${rabbitmq.virtualHost}")
    private String virtualHost;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.queue.dialog}")
    private String queueDialog;
    @Value("${rabbitmq.routingKey.dialog}")
    private String routingKeyDialog;
    @Value("${rabbitmq.queue.game}")
    private String queueGame;
    @Value("${rabbitmq.routingKey.game}")
    private String routingKeyGame;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(hostname);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        return factory;
    }
    
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }
    
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange, true, false);
    }
    
    @Bean("queueDialog")
    public Queue queueDialog() {
        return new Queue(queueDialog);
    }
    
    @Bean("queueGame")
    public Queue queueGame() {
        return new Queue(queueGame);
    }
    
    @Bean
    public Binding bindingDialog(@Qualifier("queueDialog") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKeyDialog);
    }
    
    @Bean
    public Binding bindingGame(@Qualifier("queueGame") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKeyGame);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
