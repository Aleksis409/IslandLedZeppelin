package com.javarush.island.artemov.entity.lifeforms.fauna;

import com.javarush.island.artemov.entity.lifeforms.LifeCycle;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.javarush.island.artemov.config.Default.STARVATION_WEIGHT_LOSS_PERCENT;

@Getter
public abstract class Animal extends LifeForm implements LifeCycle {
    protected Map<String, Integer> foodPreferences = new HashMap<>();

    protected Animal(String label, String name, Double weight, Integer maxPerCell, Integer maxSpeed,
                     Double foodRequiredToSaturate, String image) {
        super(label, name, weight, maxPerCell, maxSpeed, foodRequiredToSaturate, image);
    }

    public Map<String, Integer> getFoodPreferences() {
        return foodPreferences;
    }

    @Override
    public void move(Location currentLocation, GameMap gameMap) {
        int maxStep = this.getMaxSpeed();
        int currentX = currentLocation.getX();
        int currentY = currentLocation.getY();

        for (int attempt = 0; attempt < 2; attempt++) {
            int dx = ThreadLocalRandom.current().nextInt(-maxStep, maxStep + 1);
            int dy = ThreadLocalRandom.current().nextInt(-maxStep, maxStep + 1);
            int targetX = currentX + dx;
            int targetY = currentY + dy;

            if ((dx == 0 && dy == 0) || !gameMap.isWithinBounds(targetX, targetY)) {
                continue;
            }

            Location targetLocation = gameMap.getLocation(targetX, targetY);
            Location firstLock = currentLocation.hashCode() < targetLocation.hashCode() ? currentLocation : targetLocation;
            Location secondLock = currentLocation.hashCode() < targetLocation.hashCode() ? targetLocation : currentLocation;

            synchronized (firstLock) {
                synchronized (secondLock) {
                    long sameTypeCount = targetLocation.getLifeForms().stream()
                            .filter(lf -> lf.getClass().equals(this.getClass()) && lf.isAlive())
                            .count();

                    if (sameTypeCount < this.getMaxPerCell()) {
                        currentLocation.getLifeForms().remove(this);
                        targetLocation.getLifeForms().add(this);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void eat(Location location) {
        if (!isAlive || currentSaturation >= foodRequiredToSaturate) return;

        List<LifeForm> lifeForms = new ArrayList<>(location.getLifeForms());
        Collections.shuffle(lifeForms);

        for (LifeForm target : lifeForms) {
            String className = target.getClass().getSimpleName();
            int chance = foodPreferences.getOrDefault(className, 0);
            boolean shouldSkip =
                    target == this || !target.isAlive() || chance == 0
                            || ThreadLocalRandom.current().nextInt(100) >= chance;

            if (shouldSkip) {
                continue;
            }

            double requiredFood = foodRequiredToSaturate - currentSaturation;
            double foodGained = Math.min(target.getBaseWeight(), requiredFood);
            currentSaturation += foodGained;

            if (foodGained >= target.getBaseWeight()) {
                target.setAlive(false);
            }

            if (currentSaturation >= foodRequiredToSaturate) {
                break;
            }
        }
    }

    @Override
    public void reproduce(Location location) {
        if (!isAlive() || currentSaturation < foodRequiredToSaturate) return;

        long sameSpeciesCount = location.getLifeForms().stream()
                .filter(lf -> lf.getClass().equals(this.getClass()) && lf.isAlive())
                .count();

        if (sameSpeciesCount < 2 || sameSpeciesCount >= getMaxPerCell()) return;

        Animal offspring = (Animal) this.clone();
        offspring.setAlive(true);
        offspring.setCurrentWeight(this.getBaseWeight());
        offspring.setCurrentSaturation(0);
        location.addLifeForm(offspring);
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
