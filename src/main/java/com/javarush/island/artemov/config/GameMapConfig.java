package com.javarush.island.artemov.config;

import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameMapConfig {
    private int width;
    private int height;
    private double initialFillPercentage;

    public double getInitialFillPercentage() {
        return initialFillPercentage / 100.0;
    }

    public void validate() {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Размеры карты должны быть положительными.");
        }
        if (initialFillPercentage < 1 || initialFillPercentage > 100) {
            throw new IllegalArgumentException("initialFillPercentage должен быть от 1 до 100.");
        }
    }
}
