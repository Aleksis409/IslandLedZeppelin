package com.javarush.island.artemov.entity.lifeforms.fauna.herbivores;

import com.javarush.island.artemov.entity.lifeforms.LifeCycle;
import com.javarush.island.artemov.entity.lifeforms.flora.Plant;

public interface Herbivores extends LifeCycle {
    @Override
    default void eat() {

    }
}

