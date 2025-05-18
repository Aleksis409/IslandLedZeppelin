package com.javarush.island.artemov.config;

import lombok.Getter;
import java.util.Map;

@Getter
public class GameSettings {
    private GameMapConfig gameMap;
    private Map<String, LifeFormConfig> lifeForms;
}
