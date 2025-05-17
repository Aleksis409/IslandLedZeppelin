package com.javarush.island.artemov.entity.lifeforms;

import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;

public interface LifeCycle {
    void move(Location location, GameMap gameMap);

    void eat(Location location);

    void reproduce(Location location);

    void die();
}
