package com.javarush.island.artemov.entity.lifeforms.fauna.predators;

import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;

public class Wolf extends Animal implements Predators {
    protected Wolf(String label, String name, Double weight, Integer maxPerCell, Integer maxSpeed,
                   Double foodRequiredToSaturate, String image) {
        super(label, name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }
}
