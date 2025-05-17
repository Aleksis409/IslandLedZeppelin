package com.javarush.island.artemov.service.statistic;

import com.javarush.island.artemov.config.GameMapConsolePrintConfig;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.lifeforms.fauna.herbivores.Herbivores;
import com.javarush.island.artemov.entity.lifeforms.fauna.predators.Predators;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.javarush.island.artemov.config.Default.LIFE_FORM_CLASS_TYPES;

public class LifeStatistics {
    private final Map<String, LifeFormCounter> counters = new ConcurrentHashMap<>();
    private final Map<String, LifeForm> lifeFormMap;

    public LifeStatistics(Map<String, LifeForm> lifeFormMap) {
        this.lifeFormMap = lifeFormMap;
    }

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
        String className = lifeFormClass.getSimpleName().toLowerCase();
        int total = 0;

        for (Map.Entry<String, LifeFormCounter> entry : counters.entrySet()) {
            String label = entry.getKey();
            LifeFormCounter counter = entry.getValue();

            if (label.contains(className)) {
                total += counter.getAlive();
            }
        }
        return total;
    }

    public boolean hasAnyAliveAnimals() {
        for (Class<?> clazz : LIFE_FORM_CLASS_TYPES) {
            boolean isAnimal = Herbivores.class.isAssignableFrom(clazz) || Predators.class.isAssignableFrom(clazz);
            if (isAnimal && getAliveCountByClass(clazz) > 0) {
                return true;
            }
        }
        return false;
    }

    public void printToConsole() {
        int totalAlive = getTotalAlive();
        double totalWeight = getTotalAliveWeight();

        System.out.println("%n=== Статистика по живым существам ===");

        counters.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String label = entry.getKey();
                    LifeForm sample = lifeFormMap.get(label);
                    String prettyName = sample.getImage() + " " + sample.getName();
                    LifeFormCounter counter = entry.getValue();
                    int alive = counter.getAlive();
                    int dead = counter.getDead();
                    double weight = counter.getAliveWeight();
                    double percent = totalAlive == 0 ? 0 : (alive * 100.0 / totalAlive);

                    System.out.printf(
                            "- %-14s: живы: %4d (%5.2f%%), мертвы: %4d, вес: %.2f кг%n",
                            prettyName, alive, percent, dead, weight
                    );
                });

        System.out.printf("%nИтого живых существ: %d%n", totalAlive);
        System.out.printf("Суммарный вес живых существ: %.2f кг%n", totalWeight);
    }

    public void printMapView(GameMap gameMap, GameMapConsolePrintConfig config) {
        printHeader(gameMap, config);

        for (int y = config.getViewOffsetY(); y < Math.min(config.getViewOffsetY()
                + config.getViewHeight(), gameMap.getHeight()); y++) {

            List<List<String>> rowLines = buildCellVisualLines(gameMap, config, y);
            int maxLinesInCell = rowLines.stream().mapToInt(List::size).max().orElse(1);
            printCellVisualLines(rowLines, maxLinesInCell);
            printLineSeparator(config.getViewWidth());
        }
    }

    private void printHeader(GameMap gameMap, GameMapConsolePrintConfig config) {
        System.out.printf("%n=== Остров %d на %d ===", gameMap.getWidth(), gameMap.getHeight());
        System.out.printf("%n=== Область карты острова %d на %d, смещение по Х = %d, смещение по У = %d ===%n",
                config.getViewWidth(), config.getViewHeight(), config.getViewOffsetX(), config.getViewOffsetY());
    }

    private List<List<String>> buildCellVisualLines(GameMap gameMap,
                                                    GameMapConsolePrintConfig config, int y) {

        List<List<String>> cellVisualLines = new ArrayList<>();

        for (int x = config.getViewOffsetX(); x < Math.min(config.getViewOffsetX()
                + config.getViewWidth(), gameMap.getWidth()); x++) {

            Location location = gameMap.getLocation(x, y);
            Map<String, Long> countMap = location.getLifeForms().stream()
                    .filter(LifeForm::isAlive)
                    .collect(Collectors.groupingBy(LifeForm::getImage, Collectors.counting()));

            List<String> entries = countMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + e.getValue())
                    .collect(Collectors.toList());

            List<String> lines = new ArrayList<>();
            for (int i = 0; i < entries.size(); i += 3) {
                lines.add(String.join("", entries.subList(i, Math.min(i + 3, entries.size()))));
            }

            if (lines.isEmpty()) {
                lines.add("  ");
            }

            cellVisualLines.add(lines);
        }

        return cellVisualLines;
    }

    private void printLineSeparator(int viewWidth) {
        System.out.println("_".repeat(viewWidth * 11));
    }

    private void printCellVisualLines(List<List<String>> cellVisualLines, int maxLinesInCell) {
        for (int lineIndex = 0; lineIndex < maxLinesInCell; lineIndex++) {
            for (List<String> cellLines : cellVisualLines) {
                String line = lineIndex < cellLines.size() ? cellLines.get(lineIndex) : "";
                System.out.printf("|%-10s", line);
            }
            System.out.println("|");
        }
    }

    public boolean hasStabilized(int previousAlive, double previousWeight) {
        int currentAlive = getTotalAlive();
        double currentWeight = getTotalAliveWeight();
        return currentAlive == previousAlive && Double.compare(currentWeight, previousWeight) == 0;
    }
}
