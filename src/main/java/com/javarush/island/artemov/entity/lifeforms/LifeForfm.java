package com.javarush.island.artemov.entity.lifeforms;

public abstract class LifeForfm {
    protected final String name;
    protected final Double weight;
    protected final Integer maxPerCell;
    protected final Integer maxSpeed;
    protected final Double foodRequiredToSaturate;
    protected final String image;

    protected double currentSaturation = 0;
    protected boolean isAlive = true;

    protected LifeForfm(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate,
                        String image) {
        this.name = name;
        this.weight = weight;
        this.maxPerCell = maxPerCell;
        this.maxSpeed = maxSpeed;
        this.foodRequiredToSaturate = foodRequiredToSaturate;
        this.image = image;
    }
}
