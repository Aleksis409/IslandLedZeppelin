package com.javarush.island.artemov.entity.map;

import com.javarush.island.artemov.entity.lifeforms.LifeForfm;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private final int x;
    private final int y;
    private final List<LifeForfm> lifeForms = new ArrayList<>();

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addLifeForfm(LifeForfm lifeForfm) {
        lifeForms.add(lifeForfm);
    }

    public List<LifeForfm> getLifeForfm() {
        return lifeForms;
    }
}
