package com.javarush.island.artemov.service.task;

import com.javarush.island.artemov.entity.map.Location;

public interface CellTask {
    void run(Location location) throws CloneNotSupportedException;
}
