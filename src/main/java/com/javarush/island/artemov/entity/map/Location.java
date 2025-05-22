package com.javarush.island.artemov.entity.map;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private final int x;
    private final int y;
    private final List<LifeForm> lifeForms = new ArrayList<>();

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addLifeForm(LifeForm lifeForm) {
        lifeForms.add(lifeForm);
    }

    public List<LifeForm> getLifeForms() {
        return lifeForms;
    }
}
