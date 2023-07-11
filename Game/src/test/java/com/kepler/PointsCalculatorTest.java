package com.kepler;

import com.kepler.service.PointsCalculator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test case: calculate sum player points")
@Slf4j
@SpringBootTest
public class PointsCalculatorTest {
    @Autowired
    private PointsCalculator pointsCalculator;
    private String[] tokens;

    @Test
    @Order(10)
    @DisplayName("Empty tokens")
    void emptyCase() {
        tokens = new String[]{""};
        Assertions.assertEquals(pointsCalculator.process(tokens), 0);
    }

    @Test
    @Order(20)
    @DisplayName("Bull and half bull")
    void bullAndHalfBull() {
        tokens = new String[]{"буль", "и", "полубуль"};
        Assertions.assertEquals(pointsCalculator.process(tokens), 75);
    }

    @Test
    @Order(30)
    @DisplayName("x3 20")
    void multiplier() {
        tokens = new String[]{"утроение", "20"};
        Assertions.assertEquals(pointsCalculator.process(tokens), 60);
    }

    @Test
    @Order(40)
    @DisplayName("x3 20 + x2 10")
    void twoMultipliers() {
        tokens = new String[]{"утроение", "20", "и", "удвоение", "10"};
        Assertions.assertEquals(pointsCalculator.process(tokens), 80);
    }

    @Test
    @Order(50)
    @DisplayName("x3 20 + bull + half bull")
    void combineBullWithMultipliers() {
        tokens = new String[]{"утроение", "20", "буль", "и", "полубуль"};
        Assertions.assertEquals(pointsCalculator.process(tokens), 135);
    }

    @Test
    @Order(60)
    @DisplayName("Ignoring negative values")
    void negativeValue() {
        tokens = new String[]{"утроение", "минус", "20"};
        Assertions.assertEquals(pointsCalculator.process(tokens), 60);
    }

}
