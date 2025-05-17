package com.javarush.island.artemov.service.controller;

import com.javarush.island.artemov.config.GameSettings;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;
import com.javarush.island.artemov.service.phase.*;
import com.javarush.island.artemov.service.phase.StatisticsPhase;
import com.javarush.island.artemov.service.task.TaskFactory;
import com.javarush.island.artemov.service.statistic.LifeStatistics;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Getter
public class GameController {
    private final GameMap gameMap;
    private final ExecutorService executor;
    private final LifeStatistics lifeStatistics;
    private final GameSettings gameSettings;

    public GameController(GameMap gameMap, Map<String, LifeForm> lifeFormMap, GameSettings gameSettings) {
        this.gameMap = gameMap;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.lifeStatistics = new LifeStatistics(lifeFormMap);
        this.gameSettings = gameSettings;
    }

    public void runSimulationStep() throws InterruptedException {
        runPhase(new EatingPhase(gameMap));
        PlantGrowthPhase growthPhase = new PlantGrowthPhase(gameMap, lifeStatistics);
        runPhase(growthPhase);
        growthPhase.repopulateIfEmpty();
        runPhase(new MovementPhase(gameMap));
        runPhase(new ReproductionPhase(gameMap));
        runPhase(new DeathPhase(gameMap));

        lifeStatistics.reset();
        runPhase(new StatisticsPhase(gameMap, lifeStatistics));
        lifeStatistics.printMapView(gameMap, gameSettings.getGameMapConsolePrint());
        lifeStatistics.printToConsole();
    }

    private void runPhase(TaskFactory factory) throws InterruptedException {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int y = 0; y < gameMap.getHeight(); y++) {
            for (int x = 0; x < gameMap.getWidth(); x++) {
                int currentX = x;
                int currentY = y;
                tasks.add(() -> {
                    Location cell = gameMap.getLocation(currentX, currentY);
                    factory.createTask().run(cell);
                    return null;
                });
            }
        }
        executor.invokeAll(tasks);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void collectInitialStatistics() throws InterruptedException {
        runPhase(new StatisticsPhase(gameMap, lifeStatistics));
        lifeStatistics.printMapView(gameMap, gameSettings.getGameMapConsolePrint());
        lifeStatistics.printToConsole();
    }
}