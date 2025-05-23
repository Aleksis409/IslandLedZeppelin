package com.javarush.island.artemov.entity.lifeforms.flora;

import com.javarush.island.artemov.entity.lifeforms.LifeCycle;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.Location;

import java.util.concurrent.ThreadLocalRandom;

import static com.javarush.island.artemov.config.Default.STARVATION_WEIGHT_LOSS_PERCENT;

public class Plant extends LifeForm implements LifeCycle {

    protected Plant(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate,
                    String image) {
        super(name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }

    public boolean canGrow() {
        return ThreadLocalRandom.current().nextDouble() < STARVATION_WEIGHT_LOSS_PERCENT;
    }

    @Override
    public void move() {

    }

    @Override
    public void eat(Location location) {

    }


    @Override
    public void reproduce() {

    }

    @Override
    public void die() {

    }


}
