package com.javarush.island.artemov.service.statistic;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.service.task.CellTask;
import com.javarush.island.artemov.service.task.TaskFactory;

public class StatisticsPhase implements TaskFactory {
    private final GameMap gameMap;
    private final LifeStatistics statistics;

    public StatisticsPhase(GameMap gameMap, LifeStatistics statistics) {
        this.gameMap = gameMap;
        this.statistics = statistics;
    }

    @Override
    public CellTask createTask() {
        return location -> {
            for (LifeForm lifeForm : location.getLifeForms()) {
                String label = lifeForm.getImage() + " " + lifeForm.getName();
                statistics.increment(label, lifeForm.isAlive(), lifeForm.getBaseWeight());
            }
        };
    }
}
