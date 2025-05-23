package com.javarush.island.artemov.service;

import com.javarush.island.artemov.entity.map.Location;

public interface CellTask {
    void run(Location location) throws CloneNotSupportedException;
}
