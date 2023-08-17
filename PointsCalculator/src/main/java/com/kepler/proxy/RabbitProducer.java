package com.kepler.proxy;

import com.kepler.model.calculator.ResultCalculations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RabbitProducer {
    @Value("${rabbitmq.exchange}")
    private String exchange;
    
    @Value("${rabbitmq.routingKey.dialog}")
    private String routingKeyDialog;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    MessageConverter jsonMessageConverter;

    /**
     * Send to RabbitMQ queue sum of points
     * @param result
     */
    public void sendMessage(ResultCalculations result) {
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.convertAndSend(
                exchange, 
                routingKeyDialog, 
                result
        );
    }

}
