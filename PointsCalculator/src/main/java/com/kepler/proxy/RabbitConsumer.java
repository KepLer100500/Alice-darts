package com.kepler.proxy;

import com.kepler.model.calculator.ResultCalculations;
import com.kepler.model.calculator.Tokens;
import com.kepler.service.PointsCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@EnableRabbit
@Slf4j
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
        log.info("Calculations done, result: {}", result.getResultCalculations());
        rabbitProducer.sendMessage(result);
    }
}
