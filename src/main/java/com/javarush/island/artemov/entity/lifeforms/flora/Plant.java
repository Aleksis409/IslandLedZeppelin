package com.javarush.island.artemov.entity.lifeforms.flora;

import com.javarush.island.artemov.entity.lifeforms.LifeCycle;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;

public class Plant extends LifeForm implements LifeCycle {

    protected Plant(String label, String name, Double baseWeight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate, String image) {
        super(label, name, baseWeight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }

    @Override
    public void move(Location location, GameMap gameMap) {
        // Plants do not move
    }

    @Override
    public void eat(Location location) {
        // Plants do not eat
    }

    @Override
    public void reproduce(Location location) {
        // Implemented in PlantGrowthPhase
    }

    @Override
    public void die() {
        // Implemented in the Animal class eat method
    }
}
