package com.kepler;

import com.kepler.service.Bot;
import com.kepler.service.PointsCalculator;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test case: make bot random hits")
@Slf4j
@SpringBootTest
public class BotTest {
    @Autowired
    private Bot bot;

    @Autowired
    private PointsCalculator pointsCalculator;

    @Test
    @Order(10)
    @DisplayName("makeMoveTest")
    void makeMoveTest() {
        String[] tokens = bot.makeMove();
        Integer botSumPoints = pointsCalculator.process(tokens);

        Stream.of(tokens).forEach(sector -> log.info("Bot hit sector: {}", String.valueOf(sector)));
        log.info("Bot have sum points: {}", botSumPoints);

        Assertions.assertTrue(botSumPoints >= 0);
    }
}
