package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.map.GameMap;

public class MovementPhase implements TaskFactory {
    private final GameMap gameMap;

    public MovementPhase(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public CellTask createTask() {
//        return (location) -> {
//            for (Creature creature : location.getCreatures()) {
//                if (creature instanceof Animal animal) {
//                    animal.eat(location);
//                }
//            }
//        };
        return null;
    }
}
