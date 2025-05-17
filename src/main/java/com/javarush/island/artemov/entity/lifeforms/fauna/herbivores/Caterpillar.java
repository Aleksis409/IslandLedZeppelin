package com.javarush.island.artemov.entity.lifeforms.fauna.herbivores;

import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;

public class Caterpillar extends Animal implements Herbivores {
    protected Caterpillar(String label, String name, Double weight, Integer maxPerCell, Integer maxSpeed,
                          Double foodRequiredToSaturate, String image) {
        super(label, name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }
}
