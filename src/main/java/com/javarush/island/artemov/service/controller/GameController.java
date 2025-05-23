package com.javarush.island.artemov.service.controller;

import com.javarush.island.artemov.config.GameMapConfig;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;
import com.javarush.island.artemov.service.statistic.StatisticsPhase;
import com.javarush.island.artemov.service.phase.DeathPhase;
import com.javarush.island.artemov.service.phase.EatingPhase;
import com.javarush.island.artemov.service.phase.PlantGrowthPhase;
import com.javarush.island.artemov.service.phase.ReproductionPhase;
import com.javarush.island.artemov.service.task.TaskFactory;
import com.javarush.island.artemov.service.statistic.LifeStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GameController {
    private final GameMap gameMap;
    private final GameMapConfig config;
    private final ExecutorService executor;
    private final LifeStatistics lifeStatistics;

    public GameController(GameMap gameMap, GameMapConfig config) {
        this.gameMap = gameMap;
        this.config = config;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.lifeStatistics = new LifeStatistics(); // создаём один экземпляр на всё время жизни симуляции
    }

    public void runSimulationStep() throws InterruptedException {
        runPhase(new EatingPhase(gameMap));
        PlantGrowthPhase growthPhase = new PlantGrowthPhase(gameMap, lifeStatistics);
        runPhase(growthPhase);
        growthPhase.repopulateIfEmpty();
        // runPhase(new MovementPhase(gameMap)); // при необходимости раскомментируй
        runPhase(new ReproductionPhase(gameMap));
        runPhase(new DeathPhase(gameMap));

        lifeStatistics.reset(); // очищаем данные перед сбором новой статистики
        runPhase(new StatisticsPhase(gameMap, lifeStatistics));
        lifeStatistics.printToConsole(); // выводим статистику
    }

    private void runPhase(TaskFactory factory) throws InterruptedException {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                int currentX = x, currentY = y;
                tasks.add(() -> {
                    Location cell = gameMap.getLocation(currentX, currentY);
                    synchronized (cell) {
                        factory.createTask().run(cell);
                    }
                    return null;
                });
            }
        }
        executor.invokeAll(tasks);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public LifeStatistics getLifeStatistics() {
        return lifeStatistics;
    }
}