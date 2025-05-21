package com.javarush.island.artemov.config;

import com.javarush.island.artemov.entity.lifeforms.fauna.herbivores.*;
import com.javarush.island.artemov.entity.lifeforms.fauna.predators.*;
import com.javarush.island.artemov.entity.lifeforms.flora.Plant;

public class Default {
    public static final String SETTING_YAML_FILE_PATH = "artemov/setting.yaml";
    public static final String[] LIFE_FORM_NAMES = {
            "Wolf", "Boa", "Fox", "Bear", "Eagle",
            "Horse", "Deer", "Rabbit", "Mouse", "Goat", "Sheep", "Boar", "Buffalo", "Duck", "Caterpillar",
            "Plants",
    };

//    public static final Class<?>[] LIFE_FORM_CLASS_TYPES = {
//            Bear.class, Boar.class, Plant.class,
//    };

    public static final Class<?>[] LIFE_FORM_CLASS_TYPES = {
            Wolf.class, Boa.class, Fox.class, Bear.class, Eagle.class,
            Horse.class, Deer.class, Rabbit.class, Mouse.class, Goat.class, Sheep.class, Boar.class,
            Buffalo.class, Duck.class, Caterpillar.class,
            Plant.class,
    };

}
