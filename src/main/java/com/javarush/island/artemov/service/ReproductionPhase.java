package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;
import com.javarush.island.artemov.entity.map.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReproductionPhase implements TaskFactory {
    private final GameMap gameMap;

    public ReproductionPhase(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public CellTask createTask() {
        return (location) -> {
            synchronized (location) {
                Map<Class<? extends LifeForm>, List<Animal>> grouped = new HashMap<>();

                for (LifeForm lifeForm : location.getLifeForms()) {
                    if (lifeForm instanceof Animal animal && animal.isAlive() &&
                            animal.getCurrentSaturation() >= animal.getFoodRequiredToSaturate()) {
                        grouped.computeIfAbsent(animal.getClass(), k -> new ArrayList<>()).add(animal);
                    }
                }

                for (Map.Entry<Class<? extends LifeForm>, List<Animal>> entry : grouped.entrySet()) {
                    List<Animal> candidates = entry.getValue();
                    if (candidates.size() < 2) continue;

                    Animal sample = candidates.get(0);

                    long currentCount = location.getLifeForms().stream()
                            .filter(lf -> lf.getClass().equals(sample.getClass()) && lf.isAlive())
                            .count();

                    if (currentCount >= sample.getMaxPerCell()) continue;

                    Animal clone = (Animal) sample.clone();
                    clone.setCurrentSaturation(0);
                    clone.setAlive(true);
                    clone.setCurrentWeight(sample.getBaseWeight());

                    location.addLifeForm(clone);
                }
            }
        };
    }
}
