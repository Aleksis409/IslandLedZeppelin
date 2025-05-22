package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

public class StatisticsPhase implements TaskFactory{
    private final GameMap gameMap;

//    private static final Map<String, AtomicInteger> globalStatistics = new ConcurrentHashMap<>();
//    public StatisticsPhase(GameMap gameMap) {
//        this.gameMap = gameMap;
//        globalStatistics.clear();
//    }

//    @Override
//    public CellTask createTask() {
//        return location -> {
//            for (LifeForm lifeForm : location.getLifeForms()) {
//                String type = lifeForm.getName(); // например: "Wolf", "Rabbit"
//                globalStatistics
//                        .computeIfAbsent(type, k -> new AtomicInteger())
//                        .incrementAndGet();
//            }
//        };
//    }
//
//    public static void printStatistics() {
//        int total = globalStatistics.values().stream()
//                .mapToInt(AtomicInteger::get)
//                .sum();
//
//        System.out.println("\n=== Статистика по существам ===");
//        for (Map.Entry<String, AtomicInteger> entry : globalStatistics.entrySet()) {
//            String name = entry.getKey();
//            int count = entry.getValue().get();
//            double percent = total == 0 ? 0 : (count * 100.0 / total);
//            System.out.printf("%-15s: %5d (%.2f%%)%n", name, count, percent);
//        }
//        System.out.printf("Итого существ: %d%n", total);
//    }
    // Вложенная структура: image -> [живые, мёртвые]
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
                        .increment(lifeForm.isAlive(), lifeForm.getWeight());
            }
        };
    }

    public static void printStatistics() {
        int totalCount = statistics.values().stream().mapToInt(LifeFormCounter::total).sum();
        double totalWeight = statistics.values().stream().mapToDouble(LifeFormCounter::getTotalWeight).sum();

        System.out.println("\n=== 📊 Статистика по существам ===");
        statistics.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String label = entry.getKey(); // имя + иконка
                    LifeFormCounter counter = entry.getValue();

                    int alive = counter.getAlive();
                    int dead = counter.getDead();
                    int count = alive + dead;
                    double percent = totalCount == 0 ? 0 : (count * 100.0 / totalCount);
                    double weight = counter.getTotalWeight();

                    System.out.printf("- %-12s: всего: %4d (%5.2f%%), живы: %3d, мертвы: %4d, вес: %.2f кг%n",
                            label, count, percent, alive, dead, weight);
                });

        System.out.printf("\nИтого существ на карте: %d%n", totalCount);
        System.out.printf("Суммарный вес всех существ: %.2f кг%n", totalWeight);
    }

    // Вспомогательный счётчик
    private static class LifeFormCounter {
        private final LongAdder alive = new LongAdder();
        private final LongAdder dead = new LongAdder();
        private final DoubleAdder totalWeight = new DoubleAdder();

        public void increment(boolean isAlive, double weight) {
            if (isAlive) {
                alive.increment();
            } else {
                dead.increment();
            }
            totalWeight.add(weight);
        }

        public int getAlive() {
            return alive.intValue();
        }

        public int getDead() {
            return dead.intValue();
        }

        public int total() {
            return getAlive() + getDead();
        }

        public double getTotalWeight() {
            return totalWeight.doubleValue();
        }
    }

}
