package com.javarush.island.artemov.entity.map;

public class GameMap {
    private final int width;
    private final int height;
    private final Location[][] map;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new Location[height][width];
    }

    public Location getLocation(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return map[y][x];
        }
        return null;
    }
}
