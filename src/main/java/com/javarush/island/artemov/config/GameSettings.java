package com.javarush.island.artemov.config;

import lombok.Getter;

import java.util.Map;

@Getter
public class GameSettings {
    private GameMapConfig gameMapConfig;
    private Map<String, LifeFormConfig> lifeForms;
    private GameMapConsolePrintConfig gameMapConsolePrint;
}
