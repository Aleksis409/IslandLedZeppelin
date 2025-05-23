package com.javarush.island.artemov.service;

import com.javarush.island.artemov.config.GameMapConfig;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GameController {
    private final GameMap gameMap;
    private final GameMapConfig config;
    private final ExecutorService executor;

    public GameController(GameMap gameMap, GameMapConfig config) {
        this.gameMap = gameMap;
        this.config = config;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void runSimulationStep() throws InterruptedException {
        runPhase(new EatingPhase(gameMap));
        PlantGrowthPhase growthPhase = new PlantGrowthPhase(gameMap);
        runPhase(growthPhase);
        // После роста — проверка, остались ли растения
        growthPhase.repopulateIfEmpty();
//        runPhase(new MovementPhase(gameMap));
        runPhase(new ReproductionPhase(gameMap));
        runPhase(new DeathPhase(gameMap));
        StatisticsPhase.reset();
        runPhase(new StatisticsPhase(gameMap));
        StatisticsPhase.printStatistics();
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
        executor.invokeAll(tasks); // ждёт завершения всех задач
    }

    private void runSinglePhase(Runnable phase) {
        Future<?> future = executor.submit(phase);
        try {
            future.get(); // ждём завершения статистики
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
