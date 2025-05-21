package com.javarush.island.artemov.entity.lifeforms.fauna.predators;

import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;

public class Eagle extends Animal implements Predators {
    protected Eagle(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate, String image) {
        super(name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }
}
