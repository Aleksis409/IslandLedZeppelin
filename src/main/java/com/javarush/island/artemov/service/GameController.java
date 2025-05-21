package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GameController {
    private final GameMap gameMap;
    private final ExecutorService executor;

   public GameController(GameMap gameMap) {
        this.gameMap = gameMap;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void runSimulationStep() throws InterruptedException {
        runPhase(new EatingPhase(gameMap));
        runPhase(new MovementPhase(gameMap));
        runPhase(new ReproductionPhase(gameMap));
        runPhase(new DeathPhase(gameMap));
        runPhase(new StatisticsPhase(gameMap));
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
