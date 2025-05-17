package com.javarush.island.artemov;

import com.javarush.island.artemov.entity.lifeforms.LifeForfm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.service.GameInitializer;

import java.util.List;

public class ConsoleRunner {
    public static void main(String[] args) throws Exception {
        GameMap gameMap = GameInitializer.initializeGameMapFromYaml();
        System.out.println(gameMap.toString());
        List<LifeForfm> lifeForfms = GameInitializer.initializeLifeForms();
    }
}
