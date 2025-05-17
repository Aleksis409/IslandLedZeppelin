package com.javarush.island.artemov.entity.lifeforms.fauna.herbivores;

import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;

public class Boar extends Animal {

    protected Boar(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate,
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
