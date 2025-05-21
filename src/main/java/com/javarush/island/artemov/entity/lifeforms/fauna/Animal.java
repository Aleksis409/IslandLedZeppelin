package com.javarush.island.artemov.entity.lifeforms.fauna;

import com.javarush.island.artemov.entity.lifeforms.LifeCycle;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.Location;

import java.util.HashMap;
import java.util.Map;

public abstract class Animal extends LifeForm implements LifeCycle {
    protected Map<Class<? extends LifeForm>, Integer> foodPreferences = new HashMap<>();

    protected Animal(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate,
                     String image) {
        super(name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }

    public Map<Class<? extends LifeForm>, Integer> getFoodPreferences() {
        return foodPreferences;
    }

    @Override
    public void move() {

    }

    @Override
    public void eat(Location location) {

    }

    @Override
    public void reproduce() {

    }

    @Override
    public void die() {

    }
}
