package com.javarush.island.artemov.entity.lifeforms.fauna;

import com.javarush.island.artemov.entity.lifeforms.LifeCycle;
import com.javarush.island.artemov.entity.lifeforms.LifeForfm;

public abstract class Animal extends LifeForfm implements LifeCycle {

    protected Animal(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate,
                     String image) {
        super(name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }

    @Override
    public void move() {

    }

    @Override
    public void eat() {

    }

    @Override
    public void reproduce() {

    }

    @Override
    public void die() {

    }
}
