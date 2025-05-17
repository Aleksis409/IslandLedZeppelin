package com.javarush.island.artemov.util;

import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.Location;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import static com.javarush.island.artemov.config.Default.LIFE_FORM_CLASS_TYPES;

public class LifeFormUtils {

    private LifeFormUtils(){}

    public static boolean canAddMore(Location location, LifeForm sample) {
        long count = location.getLifeForms().stream()
                .filter(lf -> lf.isAlive() && lf.getClass().equals(sample.getClass()))
                .count();
        return count < sample.getMaxPerCell();
    }

    public static Map<Class<?>, Long> countAliveByClass(Location location) {
        return location.getLifeForms().stream()
                .filter(LifeForm::isAlive)
                .collect(Collectors.groupingBy(LifeForm::getClass, Collectors.counting()));
    }

    public static List<Class<?>> getAvailableLifeForms(Location location, Map<Class<?>, LifeForm> prototypes) {
        Map<Class<?>, Long> currentCounts = countAliveByClass(location);

        List<Class<?>> result = new ArrayList<>();
        for (Class<?> clazz : LIFE_FORM_CLASS_TYPES) {
            LifeForm prototype = prototypes.get(clazz);
            long current = currentCounts.getOrDefault(clazz, 0L);
            if (prototype != null && current < prototype.getMaxPerCell()) {
                result.add(clazz);
            }
        }
        return result;
    }
}
