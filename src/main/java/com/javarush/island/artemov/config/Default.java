package com.javarush.island.artemov.config;

import com.javarush.island.artemov.entity.lifeforms.fauna.herbivores.*;
import com.javarush.island.artemov.entity.lifeforms.fauna.predators.*;
import com.javarush.island.artemov.entity.lifeforms.flora.Plant;

public final class Default {
    public static final String SETTING_YAML_FILE_PATH = "artemov/setting.yaml";
    public static final Double STARVATION_WEIGHT_LOSS_PERCENT = 0.05;
    public static final Double PLANT_GROWTH_PERCENT = 0.7;

    private Default() {
    }

    public static final Class<?>[] LIFE_FORM_CLASS_TYPES = {
            Wolf.class, Boa.class, Fox.class, Bear.class, Eagle.class,
            Horse.class, Deer.class, Rabbit.class, Mouse.class, Goat.class, Sheep.class, Boar.class,
            Buffalo.class, Duck.class, Caterpillar.class,
            Plant.class,
    };
}