package com.javarush.island.artemov.service.phase;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.service.statistic.LifeStatistics;
import com.javarush.island.artemov.service.task.CellTask;
import com.javarush.island.artemov.service.task.TaskFactory;

public class StatisticsPhase implements TaskFactory {
    private final LifeStatistics statistics;

    public StatisticsPhase(GameMap gameMap, LifeStatistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public CellTask createTask() {
        return location -> {
            for (LifeForm lifeForm : location.getLifeForms()) {
                statistics.increment(lifeForm.getLabel(), lifeForm.isAlive(), lifeForm.getBaseWeight());
            }
        };
    }
}
