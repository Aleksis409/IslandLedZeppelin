package com.javarush.island.artemov.service.phase;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.lifeforms.flora.Plant;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;
import com.javarush.island.artemov.service.task.CellTask;
import com.javarush.island.artemov.service.task.TaskFactory;
import com.javarush.island.artemov.service.statistic.LifeStatistics;
import com.javarush.island.artemov.util.LifeFormUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

import static com.javarush.island.artemov.config.Default.PLANT_GROWTH_PERCENT;

public class PlantGrowthPhase implements TaskFactory {
    private final GameMap gameMap;
    private final LifeStatistics statistics;

    public PlantGrowthPhase(GameMap gameMap, LifeStatistics statistics) {
        this.gameMap = gameMap;
        this.statistics = statistics;
    }

    @Override
    public CellTask createTask() {
        return location -> {
            synchronized (location) {
                List<LifeForm> newPlants = new ArrayList<>();

                long currentCount = location.getLifeForms().stream()
                        .filter(lf -> lf instanceof Plant && lf.isAlive())
                        .count();

                Optional<LifeForm> plantSampleOpt = location.getLifeForms().stream()
                        .filter(lf -> lf instanceof Plant)
                        .findAny();

                if (plantSampleOpt.isEmpty()) return;
                LifeForm plantSample = plantSampleOpt.get();
                int maxPerCell = plantSample.getMaxPerCell();

                for (LifeForm lifeForm : location.getLifeForms()) {
                    if (!(lifeForm instanceof Plant) || !lifeForm.isAlive()) continue;

                    if (currentCount >= maxPerCell) break;

                    if (ThreadLocalRandom.current().nextDouble() < PLANT_GROWTH_PERCENT) {
                        LifeForm clone = lifeForm.clone();
                        clone.setAlive(true);
                        clone.setCurrentWeight(clone.getBaseWeight());
                        newPlants.add(clone);
                        currentCount++;
                    }

                    location.getLifeForms().addAll(newPlants);
                }
            }
        };
    }

    public void repopulateIfEmpty() {
        int alivePlants = statistics.getAliveCountByClass(Plant.class);

        if (alivePlants == 0) {
            int width = gameMap.getWidth();
            int height = gameMap.getHeight();

            for (int i = 0; i < 100; i++) {
                int x = ThreadLocalRandom.current().nextInt(width);
                int y = ThreadLocalRandom.current().nextInt(height);
                Location location = gameMap.getLocation(x, y);

                Optional<LifeForm> sampleOpt = location.getLifeForms().stream()
                        .filter(lf -> lf instanceof Plant)
                        .findAny();

                if (sampleOpt.isPresent()) {
                    LifeForm sample = sampleOpt.get();
                    if (LifeFormUtils.canAddMore(location, sample)) {
                        LifeForm clone = sample.clone();
                        clone.setAlive(true);
                        clone.setCurrentWeight(clone.getBaseWeight());
                        location.addLifeForm(clone);
                    }
                }
            }
        }
    }
}
