package com.javarush.island.artemov.entity.lifeforms;

import com.javarush.island.artemov.entity.map.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class LifeForm implements Cloneable {
    protected final String label;
    protected final String name;
    protected final Double baseWeight;
    protected final Integer maxPerCell;
    protected final Integer maxSpeed;
    protected final Double foodRequiredToSaturate;
    protected final String image;
    @Setter
    protected double currentSaturation = 0;
    @Setter
    protected boolean isAlive = true;
    @Setter
    protected double currentWeight;

    protected LifeForm(String label, String name, Double baseWeight, Integer maxPerCell, Integer maxSpeed,
                       Double foodRequiredToSaturate, String image) {
        this.label = label;
        this.name = name;
        this.baseWeight = baseWeight;
        this.maxPerCell = maxPerCell;
        this.maxSpeed = maxSpeed;
        this.foodRequiredToSaturate = foodRequiredToSaturate;
        this.image = image;
        this.currentWeight = baseWeight;
    }

    @Override
    public LifeForm clone() {
        try {
            return (LifeForm) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Ошибка клонирования LifeForm: " + getClass().getSimpleName(), e);
        }
    }

    public abstract void reproduce(Location location);
}
