package com.javarush.island.artemov.entity.map;

import lombok.Getter;

@Getter
public class GameMap {
    private final int width;
    private final int height;
    private final Location[][] map;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new Location[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = new Location(x, y);
            }
        }
    }

    public Location getLocation(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return map[y][x];
        }
        return null;
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
