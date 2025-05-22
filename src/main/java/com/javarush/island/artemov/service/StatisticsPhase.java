package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

public class StatisticsPhase implements TaskFactory {
    private final GameMap gameMap;
    private static final Map<String, LifeFormCounter> statistics = new ConcurrentHashMap<>();

    public StatisticsPhase(GameMap gameMap) {
        this.gameMap = gameMap;
        statistics.clear();
    }

    @Override
    public CellTask createTask() {
        return location -> {
            for (LifeForm lifeForm : location.getLifeForms()) {
                String label = lifeForm.getImage() + " " + lifeForm.getName();
                statistics
                        .computeIfAbsent(label, key -> new LifeFormCounter())
                        .increment(lifeForm.isAlive(), lifeForm.getBaseWeight());
            }
        };
    }

    public static void printStatistics() {
        int totalAlive = statistics.values().stream().mapToInt(LifeFormCounter::getAlive).sum();
        double totalAliveWeight = statistics.values().stream().mapToDouble(LifeFormCounter::getAliveWeight).sum();

        System.out.println("\n=== 📊 Статистика по живым существам ===");
        statistics.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String label = entry.getKey();
                    LifeFormCounter counter = entry.getValue();

                    int alive = counter.getAlive();
                    int dead = counter.getDead();
                    double weight = counter.getAliveWeight();
                    double percent = totalAlive == 0 ? 0 : (alive * 100.0 / totalAlive);

                    System.out.printf("- %-14s: живы: %4d (%5.2f%%), мертвы: %4d, вес: %.2f кг%n",
                            label, alive, percent, dead, weight);
                });

        System.out.printf("\nИтого живых существ: %d%n", totalAlive);
        System.out.printf("Суммарный вес живых существ: %.2f кг%n", totalAliveWeight);
    }

    // Вспомогательный счётчик
    private static class LifeFormCounter {
        private final LongAdder alive = new LongAdder();
        private final LongAdder dead = new LongAdder();
        private final DoubleAdder aliveWeight = new DoubleAdder();

        public void increment(boolean isAlive, double weight) {
            if (isAlive) {
                alive.increment();
                aliveWeight.add(weight);
            } else {
                dead.increment();
            }
        }

        public int getAlive() {
            return alive.intValue();
        }

        public int getDead() {
            return dead.intValue();
        }

        public double getAliveWeight() {
            return aliveWeight.doubleValue();
        }
    }
}
