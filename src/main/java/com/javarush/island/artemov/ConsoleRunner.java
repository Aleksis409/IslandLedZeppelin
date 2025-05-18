package com.javarush.island.artemov;

import com.javarush.island.artemov.config.ConfigManager;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.service.GameInitializer;

import java.util.Map;

public class ConsoleRunner {
    public static void main(String[] args) throws Exception {
        ConfigManager.loadSettings();
        GameMap gameMap = GameInitializer.initialGameMap();
    }
}
