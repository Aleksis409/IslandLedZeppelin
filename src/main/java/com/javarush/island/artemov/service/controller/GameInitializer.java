package com.javarush.island.artemov.service.controller;

import com.javarush.island.artemov.config.*;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

import static com.javarush.island.artemov.config.Default.LIFE_FORM_CLASS_TYPES;

public class GameInitializer {

    public GameMap initialGameMap() throws IOException {
        ConfigManager.loadSettings();
        GameMap gameMap = createEmptyGameMapFromYaml();
        generateInitialMapState(gameMap);
        return gameMap;
    }

    private GameMap createEmptyGameMapFromYaml() {
        GameMapConfig settings = ConfigManager.getSettings().getGameMap();
        int width = settings.getWidth();
        int height = settings.getHeight();
        return new GameMap(width, height);
    }

    private void generateInitialMapState(GameMap gameMap) {
        Map<String, LifeForm> lifeFormMap = initializeLifeFormsMap();
        List<String> lifeFormKeys = new ArrayList<>(lifeFormMap.keySet());
        Random random = new Random();

        List<Point> randomCells = getRandomCoordinates(gameMap);

        for (Point point : randomCells) {
            Location cell = gameMap.getLocation(point.x, point.y);

            int count = 1 + random.nextInt(lifeFormKeys.size());
//            Set<String> selectedTypes = RandomSelection.getRandomTypes(lifeFormKeys, count);
            Set<String> selectedTypes = new HashSet<>();
            selectedTypes.add("Растения");
            selectedTypes.add("Лошадь");
            selectedTypes.add("Волк");

            for (String type : selectedTypes) {
                LifeForm prototype = lifeFormMap.get(type);
                int amount = 1 + random.nextInt(prototype.getMaxPerCell());

                for (int i = 0; i < amount; i++) {
                    LifeForm instance = cloneLifeForm(prototype);
                    cell.addLifeForm(instance);
                }
            }
        }
    }

    private LifeForm cloneLifeForm(LifeForm original) {
        try {
            return original.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("LifeForm не поддерживает клонирование", e);
        }
    }

    private List<Point> getRandomCoordinates(GameMap gameMap) {
        GameMapConfig settings = ConfigManager.getSettings().getGameMap();
        int totalCell = gameMap.getWidth() * gameMap.getHeight();
        double initialFillPercentage = settings.getInitialFillPercentage();
        int cellsToFill = (int) Math.ceil(totalCell * initialFillPercentage);

        Set<Point> randomCoordinates = new HashSet<>();
        Random random = new Random();

        while (randomCoordinates.size() < cellsToFill) {
            int x = random.nextInt(gameMap.getWidth());
            int y = random.nextInt(gameMap.getHeight());
            randomCoordinates.add(new Point(x, y));
        }
        return new ArrayList<>(randomCoordinates);
    }


    private Map<String, LifeForm> initializeLifeFormsMap() {
        Map<String, LifeFormConfig> settingsMap = ConfigManager.getSettings().getLifeForms();
        Map<String, LifeForm> lifeFormMap = new HashMap<>();

        for (Class<?> clazz : LIFE_FORM_CLASS_TYPES) {
            createLifeForm(clazz, settingsMap).ifPresent(lifeForm ->
                    lifeFormMap.put(lifeForm.getName(), lifeForm)
            );
        }
        return lifeFormMap;
    }

    private Optional<LifeForm> createLifeForm(Class<?> clazz, Map<String, LifeFormConfig> settingsMap) {
        try {
            String key = clazz.getSimpleName().toLowerCase();
            LifeFormConfig config = settingsMap.get(key);

            if (config == null) {
                System.err.println("Нет конфигурации для класса: " + key);
                return Optional.empty();
            }

            LifeForm instance = getLifeForm(clazz, config);
            return Optional.of(instance);
        } catch (Exception e) {
            System.err.println("Ошибка создания LifeForm для класса: " + clazz.getSimpleName());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private LifeForm getLifeForm(Class<?> clazz, LifeFormConfig config)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {

        // 1. Ищем подходящий конструктор
        Constructor<?> constructor = clazz.getDeclaredConstructor(
                String.class, Double.class, Integer.class, Integer.class, Double.class, String.class
        );
        constructor.setAccessible(true);

        // 2. Создаем экземпляр
        LifeForm instance = (LifeForm) constructor.newInstance(
                config.getName(),
                config.getWeight(),
                config.getMaxPerCell(),
                config.getMaxSpeed(),
                config.getFoodToSaturate(),
                config.getImage()
        );

        // 3. Если это Animal — устанавливаем foodPreferences
        if (instance instanceof Animal animal && config.getEats() != null) {
            for (Map.Entry<String, Integer> entry : config.getEats().entrySet()) {
                String className = capitalize(entry.getKey()); // "rabbit" → "Rabbit"
                animal.getFoodPreferences().put(className, entry.getValue());
            }
        }

        return instance;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
