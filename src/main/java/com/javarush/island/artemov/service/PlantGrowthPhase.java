package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.lifeforms.flora.Plant;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;
import com.javarush.island.artemov.util.LifeFormUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

import static com.javarush.island.artemov.config.Default.PLANT_GROWTH_PERCENT;

public class PlantGrowthPhase implements TaskFactory {
    private final GameMap gameMap;

    public PlantGrowthPhase(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public CellTask createTask() {
        return location -> {
            synchronized (location) {
                List<LifeForm> newPlants = new ArrayList<>();

                // Текущее количество живых растений
                long currentCount = location.getLifeForms().stream()
                        .filter(lf -> lf instanceof Plant && lf.isAlive())
                        .count();

                // Берём один образец растения (предполагается, что они все одинаковые)
                Optional<LifeForm> plantSampleOpt = location.getLifeForms().stream()
                        .filter(lf -> lf instanceof Plant)
                        .findAny();

                if (plantSampleOpt.isEmpty()) return;
                LifeForm plantSample = plantSampleOpt.get();
                int maxPerCell = plantSample.getMaxPerCell();

                for (LifeForm lifeForm : location.getLifeForms()) {
                    if (!(lifeForm instanceof Plant) || !lifeForm.isAlive()) continue;

                    if (currentCount >= maxPerCell) break; // лимит достигнут

                    if (ThreadLocalRandom.current().nextDouble() < PLANT_GROWTH_PERCENT) {
                        try {
                            LifeForm clone = lifeForm.clone();
                            clone.setAlive(true);
                            clone.setCurrentWeight(clone.getBaseWeight());
                            newPlants.add(clone);
                            currentCount++; // увеличиваем счётчик
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                location.getLifeForms().addAll(newPlants);
            }
        };
    }

    public void repopulateIfEmpty() {
        int alivePlants = StatisticsPhase.getAliveCountByClass(Plant.class);

        if (alivePlants == 0) {
            int width = gameMap.getWidth();
            int height = gameMap.getHeight();

            for (int i = 0; i < 100; i++) { // допустим, хотим посадить 100 растений
                int x = ThreadLocalRandom.current().nextInt(width);
                int y = ThreadLocalRandom.current().nextInt(height);
                Location location = gameMap.getLocation(x, y);

                // ищем образец растения
                Optional<LifeForm> sampleOpt = location.getLifeForms().stream()
                        .filter(lf -> lf instanceof Plant)
                        .findAny();

                if (sampleOpt.isPresent()) {
                    LifeForm sample = sampleOpt.get();
                    if (LifeFormUtils.canAddMore(location, sample)) {
                        try {
                            LifeForm clone = sample.clone();
                            clone.setAlive(true);
                            clone.setCurrentWeight(clone.getBaseWeight());
                            location.addLifeForm(clone);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
