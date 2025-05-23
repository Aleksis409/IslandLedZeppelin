package com.javarush.island.artemov.entity.lifeforms.fauna;

import com.javarush.island.artemov.entity.lifeforms.LifeCycle;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.Location;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.javarush.island.artemov.config.Default.STARVATION_WEIGHT_LOSS_PERCENT;

public abstract class Animal extends LifeForm implements LifeCycle {
    protected Map<String, Integer>foodPreferences = new HashMap<>();

    protected Animal(String name, Double weight, Integer maxPerCell, Integer maxSpeed, Double foodRequiredToSaturate,
                     String image) {
        super(name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }

    public Map<String, Integer> getFoodPreferences() {
        return foodPreferences;
    }

    @Override
    public void move() {

    }

    @Override
    public void eat(Location location) {
        if (!isAlive || currentSaturation >= foodRequiredToSaturate) return;

        List<LifeForm> lifeForms = new ArrayList<>(location.getLifeForms());
        Collections.shuffle(lifeForms); // случайный порядок

        for (LifeForm target : lifeForms) {
            if (target == this || !target.isAlive()) continue;

            String className = target.getClass().getSimpleName();
            int chance = foodPreferences.getOrDefault(className, 0);
            if (chance == 0) continue;

            boolean success = ThreadLocalRandom.current().nextInt(100) < chance;
            if (success) {
                double foodGained = Math.min(target.getBaseWeight(), foodRequiredToSaturate - currentSaturation);
                currentSaturation += foodGained;

                if (foodGained >= target.getBaseWeight()) {
                    target.setAlive(false);
                }

                if (currentSaturation >= foodRequiredToSaturate) break;
            }
        }
    }

    @Override
    public void reproduce() {

    }

    @Override
    public void die() {
        if (!isAlive) return;

        if (currentSaturation < foodRequiredToSaturate) {
            double weightLoss = baseWeight * STARVATION_WEIGHT_LOSS_PERCENT;
            currentWeight -= weightLoss;

            if (currentWeight <= 0) {
                currentWeight = 0;
                isAlive = false;
            }
        }
        currentSaturation = 0;
    }
}
