package com.javarush.island.artemov.entity.lifeforms;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class LifeForm implements Cloneable {

    protected final String name;
    protected final Double weight;
    protected final Integer maxPerCell;
    protected final Integer maxSpeed;
    protected final Double foodRequiredToSaturate;
    protected final String image;
    @Setter
    protected double currentSaturation = 0;
    @Setter
    protected boolean isAlive = true;

    protected LifeForm(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate,
                       String image) {
        this.name = name;
        this.weight = weight;
        this.maxPerCell = maxPerCell;
        this.maxSpeed = maxSpeed;
        this.foodRequiredToSaturate = foodRequiredToSaturate;
        this.image = image;
    }

    @Override
    public LifeForm clone() throws CloneNotSupportedException {
        return (LifeForm) super.clone();
    }
}
