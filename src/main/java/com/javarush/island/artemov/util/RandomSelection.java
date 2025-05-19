package com.javarush.island.artemov.util;

import java.util.*;

public class RandomSelection {
    public static List<Integer> getRandomNumbers( int total, int count) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers.subList(0, count);
    }

    public static Set<String> getRandomTypes(List<String> types, int count) {
        Collections.shuffle(types);
        return new HashSet<>(types.subList(0, count));
    }
}
