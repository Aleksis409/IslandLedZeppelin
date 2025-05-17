package com.javarush.island.artemov;

import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.service.controller.GameController;
import com.javarush.island.artemov.service.controller.GameInitializer;


public class SimulationConsoleRunner {
    public static void main(String[] args) {
        try {
            GameInitializer initializer = new GameInitializer();
            GameMap gameMap = initializer.initialGameMap();
            GameController controller = new GameController(gameMap, initializer.getLifeFormMap(), initializer.getGameSettings());

            controller.collectInitialStatistics();

            int step = 0;
            int previousAlive = -1;
            double previousWeight = -1.0;

            while (controller.getLifeStatistics().hasAnyAliveAnimals()) {
                controller.runSimulationStep();
                Thread.sleep(100);

                if (controller.getLifeStatistics().hasStabilized(previousAlive, previousWeight)) {
                    System.out.println("Нет изменений на карте. Симуляция остановлена.");
                    break;
                }

                previousAlive = controller.getLifeStatistics().getTotalAlive();
                previousWeight = controller.getLifeStatistics().getTotalAliveWeight();
                step++;
                System.out.println("Цикл " + step);
            }

            if (!controller.getLifeStatistics().hasAnyAliveAnimals()) {
                System.out.println("%nВсе животные вымерли 💀. Симуляция остановлена.");
            }

            System.out.println("Количество циклов: " + step);
            controller.shutdown();

        } catch (RuntimeException e) {
            System.err.println("Ошибка запуска симуляции: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Симуляция была прервана.");
            Thread.currentThread().interrupt();
        }
    }
}