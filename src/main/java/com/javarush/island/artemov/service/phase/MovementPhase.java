package com.javarush.island.artemov.service.phase;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.service.task.CellTask;
import com.javarush.island.artemov.service.task.TaskFactory;

import java.util.ArrayList;
import java.util.List;

public class MovementPhase implements TaskFactory {
    private final GameMap gameMap;

    public MovementPhase(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public CellTask createTask() {
        return location -> {
            List<LifeForm> copy = new ArrayList<>(location.getLifeForms());

            for (LifeForm lifeForm : copy) {
                if (lifeForm instanceof Animal animal && animal.isAlive() && animal.getMaxSpeed() > 0) {
                    animal.move(location, gameMap);
                }
            }
        };
    }
}
