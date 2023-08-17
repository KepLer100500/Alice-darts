package com.kepler.proxy;

import com.kepler.model.calculator.ResultCalculations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.amqp.support.converter.MessageConverter;

@Service
@Slf4j
public class RabbitConsumer {
    @Value("${rabbitmq.queue.dialog}")
    private String queueDialog;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter jsonMessageConverter;

    /**
     * Get from RabbitMQ queue result calculations - sum player points
     * @return ResultCalculations
     */
    public ResultCalculations receiveMessage() {
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        ResultCalculations result = (ResultCalculations)rabbitTemplate.receiveAndConvert(queueDialog, 1000);
        log.info("Receive message: {}", result);
        return result;
    }
}
