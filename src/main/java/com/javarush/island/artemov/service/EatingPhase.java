package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;
import com.javarush.island.artemov.entity.map.GameMap;

public class EatingPhase implements TaskFactory {
    private final GameMap gameMap;

    public EatingPhase(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public CellTask createTask() {
        return (location) -> {
            for (LifeForm lifeForm : location.getLifeForms()) {
                if (lifeForm instanceof Animal animal) {
                    animal.eat(location);
                }
            }
        };
    }
}

