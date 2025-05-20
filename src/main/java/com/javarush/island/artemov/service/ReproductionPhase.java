package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.map.GameMap;

public class ReproductionPhase implements TaskFactory{
    private final GameMap gameMap;

    public ReproductionPhase(GameMap gameMap) {
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
