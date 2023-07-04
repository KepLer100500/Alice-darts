package com.kepler.alicedarts;

import com.kepler.alicedarts.service.PointsCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test case: Get previous, current and next values from list")
@Log
public class PointsCalculatorTest {
    private static ListIterator<String> wordsIterator;
    private static HashMap<String, String> values;

    @BeforeAll
    static void globalSetup() {
        List<String> words = new LinkedList<>(List.of("20", "Bull", "15"));
        wordsIterator = words.listIterator();
    }

    @BeforeEach
    void getValuesFromIterator() {
        values = PointsCalculator.getPreviousCurrentNextValues(wordsIterator);
    }

    @Test
    @Order(10)
    @DisplayName("Try to get first element")
    void getFirstValuesTest() {
        Assertions.assertNull(values.get("previous"));
        Assertions.assertEquals(values.get("current"), "20");
        Assertions.assertEquals(values.get("next"), "Bull");
    }

    @Test
    @Order(20)
    @DisplayName("Try to get middle element")
    void getMiddleValuesTest() {
        Assertions.assertEquals(values.get("previous"), "20");
        Assertions.assertEquals(values.get("current"), "Bull");
        Assertions.assertEquals(values.get("next"), "15");
    }

    @Test
    @Order(30)
    @DisplayName("Try to get last element")
    void getLastValuesTest() {
        Assertions.assertEquals(values.get("previous"), "Bull");
        Assertions.assertEquals(values.get("current"), "15");
        Assertions.assertNull(values.get("next"));
    }
}
