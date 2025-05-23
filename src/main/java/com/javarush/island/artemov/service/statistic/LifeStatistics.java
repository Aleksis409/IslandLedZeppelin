package com.javarush.island.artemov.service.statistic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LifeStatistics {
    private final Map<String, LifeFormCounter> counters = new ConcurrentHashMap<>();

    public void increment(String label, boolean isAlive, double weight) {
        counters
                .computeIfAbsent(label, key -> new LifeFormCounter())
                .increment(isAlive, weight);
    }

    public void reset() {
        counters.clear();
    }

    public int getTotalAlive() {
        return counters.values().stream().mapToInt(LifeFormCounter::getAlive).sum();
    }

    public double getTotalAliveWeight() {
        return counters.values().stream().mapToDouble(LifeFormCounter::getAliveWeight).sum();
    }

    public int getAliveCountByClass(Class<?> lifeFormClass) {
        return counters.entrySet().stream()
                .filter(e -> e.getKey().contains(lifeFormClass.getSimpleName()))
                .mapToInt(e -> e.getValue().getAlive())
                .sum();
    }

    public void printToConsole() {
        int totalAlive = getTotalAlive();
        double totalWeight = getTotalAliveWeight();

        System.out.println("\n=== 📊 Статистика по живым существам ===");

        counters.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String label = entry.getKey();
                    LifeFormCounter counter = entry.getValue();
                    int alive = counter.getAlive();
                    int dead = counter.getDead();
                    double weight = counter.getAliveWeight();
                    double percent = totalAlive == 0 ? 0 : (alive * 100.0 / totalAlive);

                    System.out.printf(
                            "- %-14s: живы: %4d (%5.2f%%), мертвы: %4d, вес: %.2f кг%n",
                            label, alive, percent, dead, weight
                    );
                });

        System.out.printf("\nИтого живых существ: %d%n", totalAlive);
        System.out.printf("Суммарный вес живых существ: %.2f кг%n", totalWeight);
    }
}
