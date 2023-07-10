package com.kepler.proxy;

import com.kepler.model.ResultCalculations;
import com.kepler.model.Tokens;
import com.kepler.service.PointsCalculator;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;


@Service
@EnableRabbit
@Log
public class RabbitConsumer {
    @Autowired
    RabbitProducer rabbitProducer;
    
    @Autowired
    PointsCalculator pointsCalculator;

    /**
     * Get words from RabbitMQ queue, calculate sum of points and send result
     * @param tokens
     */
    @RabbitListener(queues = "${rabbitmq.queue.game}", messageConverter = "jsonMessageConverter")
    public void processQueue(Tokens tokens) {
        ResultCalculations result = ResultCalculations.builder()
                .resultCalculations(
                        pointsCalculator.process(tokens.getTokens())
                ).build();
        log.log(Level.INFO, "Calculations done, result: {0}", result.getResultCalculations());
        rabbitProducer.sendMessage(result);
    }
}
