package com.javarush.island.artemov.entity.lifeforms.fauna.herbivores;

import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;

public class Buffalo extends Animal implements Herbivores {
    protected Buffalo(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate, String image) {
        super(name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }
}
