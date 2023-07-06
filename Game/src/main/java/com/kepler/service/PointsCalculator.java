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
    public HashMap<String, String> getPreviousCurrentNextValues(ListIterator<String> wordsIterator) {
        /*
        Get previous, current, next values relatively current word
         */
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

    private int bullOrHalfBull(String word) {
        /*
        Check, player hit bull or half-bull
         */
        if(word.equals("полубуль")) {
            return 25;
        }
        if(word.equals("буль")) {
            return 50;
        }
        return 0;
    }

    private int multipliedSector(String multiplier, int sector) {
        /*
        Check, player hit sector with multiplier
         */
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

    private int multipliedBullOrHalfBull(int multiplier, String sector) {
        /*
        Check, how many times' player hit bull or half-bull
         */
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

    private int getMultipliedValueIfExist(HashMap<String, String> values, int curNumber) {
        /*
        Calculate, where hit player
         */
        return bullOrHalfBull(values.get("current")) +
                multipliedSector(values.get("previous"), curNumber) +
                multipliedBullOrHalfBull(curNumber, values.get("next"));
    }

    public Integer process(String[] tokens) {
        int result = 0;
        List<String> words = new ArrayList<>(List.of(tokens));
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
