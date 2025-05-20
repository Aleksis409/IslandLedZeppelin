package com.javarush.island.artemov;

import com.javarush.island.artemov.config.ConfigManager;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.service.GameInitializer;


public class ConsoleRunner {
    public static void main(String[] args) throws Exception {
        GameInitializer game = new GameInitializer();
        ConfigManager.loadSettings();
        GameMap gameMap = game.initialGameMap();
    }
}
