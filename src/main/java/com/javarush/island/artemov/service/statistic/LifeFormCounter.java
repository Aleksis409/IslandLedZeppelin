package com.javarush.island.artemov.service.statistic;

import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

public class LifeFormCounter {
    private final LongAdder alive = new LongAdder();
    private final LongAdder dead = new LongAdder();
    private final DoubleAdder aliveWeight = new DoubleAdder();

    public void increment(boolean isAlive, double weight) {
        if (isAlive) {
            alive.increment();
            aliveWeight.add(weight);
        } else {
            dead.increment();
        }
    }

    public int getAlive() {
        return alive.intValue();
    }

    public int getDead() {
        return dead.intValue();
    }

    public double getAliveWeight() {
        return aliveWeight.doubleValue();
    }
}
