package com.javarush.island.artemov.entity.lifeforms.fauna.herbivores;

import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;

public class Horse extends Animal implements Herbivores{
    protected Horse(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate, String image) {
        super(name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }
}
