package com.kepler.proxy;

import com.kepler.model.Tokens;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.logging.Level;


@Service
@Log
public class RabbitProducer {
    @Value("${rabbitmq.exchange}")
    private String exchange;
    
    @Value("${rabbitmq.routingKey.game}")
    private String routingKeyGame;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private MessageConverter jsonMessageConverter;

    /**
     * Send from RabbitMQ queue, array of words for calculate sum player points
     * @param tokens
     */
    public void sendMessage(Tokens tokens) {
        log.log(Level.INFO, "Sending message: {0}", tokens);

        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.convertAndSend(
                exchange, 
                routingKeyGame, 
                tokens
        );
    }
}
