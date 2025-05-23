package com.javarush.island.artemov;

import com.javarush.island.artemov.config.ConfigManager;
import com.javarush.island.artemov.config.GameMapConfig;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.service.GameController;
import com.javarush.island.artemov.service.GameInitializer;
import com.javarush.island.artemov.service.StatisticsPhase;


public class ConsoleRunner {
    public static void main(String[] args) throws Exception {

        GameMapConfig config = new GameMapConfig();
        GameInitializer game = new GameInitializer();
        GameMap gameMap = game.initialGameMap();

        GameController controller = new GameController(gameMap, config);

        for (int i = 0; i < 100; i++) {
            controller.runSimulationStep();
            Thread.sleep(100); // задержка между шагами
            if (StatisticsPhase.getTotalAlive() == 0) {
                System.out.println("💀 Все существа умерли. Игра завершена.");
                System.out.println("Количество циклов - " + i);
                break;
            }
        }

        controller.shutdown();
    }
}
