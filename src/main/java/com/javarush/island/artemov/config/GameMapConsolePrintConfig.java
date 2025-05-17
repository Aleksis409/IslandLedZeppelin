package com.javarush.island.artemov.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javarush.island.artemov.entity.map.GameMap;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameMapConsolePrintConfig {
    private int viewWidth;
    private int viewHeight;
    private int viewOffsetX;
    private int viewOffsetY;

    public void validateAgainstMap(GameMap map) {
        if (viewWidth <= 0 || viewHeight <= 0) {
            throw new IllegalArgumentException("Размеры области просмотра должны быть положительными");
        }

        if (viewWidth > map.getWidth() || viewHeight > map.getHeight()) {
            throw new IllegalArgumentException("Размеры области просмотра превышают размеры карты");
        }

        if (viewOffsetX < 0 || viewOffsetY < 0) {
            throw new IllegalArgumentException("Смещения должны быть положительными");
        }

        if (viewOffsetX + viewWidth > map.getWidth() || viewOffsetY + viewHeight > map.getHeight()) {
            throw new IllegalArgumentException("Область просмотра выходит за границы карты");
        }
    }
}