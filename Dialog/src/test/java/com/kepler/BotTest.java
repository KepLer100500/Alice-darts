package com.kepler;

import com.kepler.model.calculator.ResultCalculations;
import com.kepler.model.calculator.Tokens;
import com.kepler.proxy.RabbitConsumer;
import com.kepler.proxy.RabbitProducer;
import com.kepler.service.Bot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.stream.Stream;


/**
 * Before run this test, run PointsCalculator module!
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Integration test case: make bot random hits.")
@Slf4j
@SpringBootTest
public class BotTest {
    @Autowired
    private Bot bot;

    @Autowired
    private RabbitProducer rabbitProducer;

    @Autowired
    private RabbitConsumer rabbitConsumer;

    @Test
    @Order(10)
    @DisplayName("makeMoveTest")
    @Disabled
    void makeMoveTest() {
        String[] tokens = bot.makeMove();
        Integer botSumPoints = sendTextAndReceiveCalculatedPoints(tokens);

        Stream.of(tokens).forEach(sector -> log.info("Bot hit sector: {}", String.valueOf(sector)));
        log.info("Bot have sum points: {}", botSumPoints);

        Assertions.assertTrue(botSumPoints >= 0);
    }

    /**
     * Send array words through rabbitmq, calculate sum points and get it
     * @param tokens
     * @return Integer result
     */
    private Integer sendTextAndReceiveCalculatedPoints(String[] tokens) {
        rabbitProducer.sendMessage(
                Tokens.builder().tokens(tokens).build()
        );
        ResultCalculations result = rabbitConsumer.receiveMessage();
        return result.getResultCalculations();
    }

}
