package com.javarush.island.artemov.entity.lifeforms.flora;

import com.javarush.island.artemov.entity.lifeforms.LifeCycle;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;

public class Plant extends LifeForm implements LifeCycle {

    protected Plant(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate,
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
