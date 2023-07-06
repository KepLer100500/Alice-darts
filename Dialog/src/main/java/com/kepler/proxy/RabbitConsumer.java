package com.kepler.proxy;

import com.kepler.model.rabbitExchange.ResultCalculations;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import java.util.logging.Level;


@Service
@EnableRabbit
@Log
public class RabbitConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.dialog}", messageConverter = "jsonMessageConverter")
    public void processQueue(ResultCalculations result) {
        log.log(Level.INFO, "Result calculations: {0}", 
                result.getResultCalculations());
    }
}
