package com.javarush.island.artemov.entity.lifeforms;

import com.javarush.island.artemov.entity.map.Location;

public interface LifeCycle {
    void move();
    void eat(Location location);

    void reproduce();
    void die();
}
