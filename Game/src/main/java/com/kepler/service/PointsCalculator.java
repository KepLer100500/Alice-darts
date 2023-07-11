package com.kepler.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;


@Log
@Service
public class PointsCalculator {
    /**
     * Get previous, current, next values relatively current word
     * @param wordsIterator
     * @return hashMap - values
     */
    private HashMap<String, String> getPreviousCurrentNextValues(ListIterator<String> wordsIterator) {
        HashMap<String, String> values = new HashMap<>();
        String curWord;
        String prevWord;
        String nextWord;
        if(wordsIterator.hasPrevious()) {
            prevWord = wordsIterator.previous();
            wordsIterator.next();
        } else {
            prevWord = null;
        }
        curWord = wordsIterator.next();
        if(wordsIterator.hasNext()) {
            nextWord = wordsIterator.next();
            wordsIterator.previous();
        } else {
            nextWord = null;
        }
        values.put("current", curWord);
        values.put("previous", prevWord);
        values.put("next", nextWord);
        return values;
    }

    /**
     * Check, player hit bull or half-bull
     * @param word
     * @return points
     */
    private int bullOrHalfBull(String word) {
        if(word.equals("полубуль")) {
            return 25;
        }
        if(word.equals("буль")) {
            return 50;
        }
        return 0;
    }

    /**
     * Check, player hit sector with multiplier
     * @param multiplier
     * @param sector
     * @return points
     */
    private int multipliedSector(String multiplier, int sector) {
        if(multiplier != null) {
            if (multiplier.equals("удвоение")) {
                return sector * 2;
            }
            if (multiplier.equals("утроение")) {
                return sector * 3;
            }
        }
        return 0;
    }

    /**
     * Check, player hit bull or half bull with multiplier
     * @param multiplier
     * @param sector
     * @return points
     */
    private int multipliedBullOrHalfBull(int multiplier, String sector) {
        if(sector != null) {
            if (sector.equals("буля")) {
                return multiplier * 50;
            }
            if (sector.equals("полубуля")) {
                return multiplier * 25;
            }
        }
        return 0;
    }

    /**
     * Calculate where hit player, multiply sector, or bull, or multiply bull
     * @param values
     * @param curNumber
     * @return points
     */
    private int getMultipliedValueIfExist(HashMap<String, String> values, int curNumber) {
        /*
        Calculate, where hit player
         */
        return bullOrHalfBull(values.get("current")) +
                multipliedSector(values.get("previous"), curNumber) +
                multipliedBullOrHalfBull(curNumber, values.get("next"));
    }

    /**
     * Get words, calculate sum player points
     * @param tokens
     * @return sum all points
     */
    public Integer process(String[] tokens) {
        int result = 0;
        List<String> words = new ArrayList<>(List.of(tokens));
        words.removeIf(word -> word.equalsIgnoreCase("минус")); // filter negative values
        ListIterator<String> wordsIterator = words.listIterator();
        HashMap<String, String> values;
        while(wordsIterator.hasNext()) {
            values = getPreviousCurrentNextValues(wordsIterator); // try to get previous and next values

            int curNumber = 0;
            try {
                curNumber = Integer.parseInt(values.get("current")); // convert string word into int value if it is possible
            } catch (NumberFormatException ignored) {}

            int multipliedValue = getMultipliedValueIfExist(values, curNumber);
            // if player not hit any multiplier sector or bull or half-bull, player get scopes for single sector
            if(multipliedValue == 0) {
                result += curNumber;
            } else {
                result += multipliedValue;
            }
        }
        return result;
    }
}
