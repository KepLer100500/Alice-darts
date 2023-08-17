package com.kepler.service;

import com.kepler.model.bot.Sector;
import lombok.extern.slf4j.Slf4j;
import org.ini4j.Wini;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Slf4j
@Service
public class Bot {

    /**
     * Generate array bot hits
     * @return tokens
     */
    public String[] makeMove() {
        List<String> botTokens = makeRandomHits(
                makeBotTableChanceToHitSectors()
        );
        return botTokens.toArray(String[]::new);
    }

    /**
     * Calculate random chance by take multiplier
     * @param sector
     * @return multiplier
     */
    private String multiplierChance(String sector) {
        if(sector.equalsIgnoreCase("полубуль") || //skip bull and half bull
                sector.equalsIgnoreCase("буль")) {
            return "";
        } else {
            if(new Random().nextInt(15) == 0) { // 15% -> x2
                return "удвоение";
            } else if(new Random().nextInt(10) == 0) { // 10% -> x3
                return "утроение";
            } else {
                return ""; // bad luck
            }
        }
    }

    /**
     * Algorithm take random chance by hit sector
     * @param sectors
     * @return list hits
     */
    private List<String> makeRandomHits(List<Sector> sectors) {
        List<String> result = new ArrayList<>();
        for (int j = 0; j < 3; j++) { // 3 darts
            int randInt = new Random().nextInt(100) + 1; // rand int 1 .. 100
            int sumChances = 0;
            for (int i = 0; i < sectors.size(); i++) {
                sumChances += sectors.get(i).getChance();
                if (100 - sumChances < randInt) {
                    result.add(multiplierChance(sectors.get(i).getTitle())); // take chance by multiplier
                    result.add(sectors.get(i).getTitle()); // add hitted sector
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Read ini file with settings % chance for hit any sectors
     * @return list chances
     */
    private List<Sector> makeBotTableChanceToHitSectors() {
        List<Sector> sectors = new ArrayList<>();
        Wini ini = null;
        try {
            ini = new Wini(new File("src/main/resources/sectorChance.ini"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i <= 20; i++) {
            int chance = ini.get("easy-bot", String.valueOf(i), int.class);
            sectors.add(Sector.builder().title(String.valueOf(i)).chance(chance).build());
        }
        sectors.add(Sector.builder().title("полубуль").chance(ini.get("easy-bot", "half-bull", int.class)).build());
        sectors.add(Sector.builder().title("буль").chance(ini.get("easy-bot", "bull", int.class)).build());

        reverseSortListByChance(sectors); // need reverse sort list for found chance hit sector
        return sectors;
    }

    /**
     * Reverse sort list chances
     * @param sectors
     */
    private void reverseSortListByChance(List<Sector> sectors) {
        sectors.sort(((o1, o2) -> {
            if (o1.getChance() < o2.getChance()) {
                return 1;
            } else if (o1.getChance() > o2.getChance()) {
                return -1;
            } else {
                return 0;
            }
        }));
    }

    
}
