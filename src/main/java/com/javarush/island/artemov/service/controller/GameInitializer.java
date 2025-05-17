package com.javarush.island.artemov.service.controller;

import com.javarush.island.artemov.config.*;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.lifeforms.fauna.Animal;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;
import com.javarush.island.artemov.exception.GameInitializationException;
import lombok.Getter;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.javarush.island.artemov.config.Default.LIFE_FORM_CLASS_TYPES;

@Getter
public class GameInitializer {
    private Map<String, LifeForm> lifeFormMap;
    private GameSettings gameSettings;

    public GameMap initialGameMap() {
        try {
            ConfigManager.loadSettings();
            GameSettings settings = ConfigManager.getSettings();
            this.gameSettings = ConfigManager.getSettings();
            settings.getGameMapConfig().validate();
            this.lifeFormMap = initializeLifeFormsMap();
            GameMap gameMap = createEmptyGameMapFromYaml();
            settings.getGameMapConsolePrint().validateAgainstMap(gameMap);
            generateInitialMapState(gameMap);
            return gameMap;
        } catch (Exception e) {
            throw new GameInitializationException("Ошибка инициализации карты", e);
        }
    }

    private GameMap createEmptyGameMapFromYaml() {
        GameMapConfig settings = ConfigManager.getSettings().getGameMapConfig();
        int width = settings.getWidth();
        int height = settings.getHeight();
        return new GameMap(width, height);
    }

    private void generateInitialMapState(GameMap gameMap) {
        Map<String, LifeForm> lifeFormsMap = initializeLifeFormsMap();
        List<String> lifeFormsKeys = new ArrayList<>(lifeFormsMap.keySet());

        List<Point> randomCells = getRandomCoordinates(gameMap);

        for (Point point : randomCells) {
            Location cell = gameMap.getLocation(point.x, point.y);

            int count = 1 + ThreadLocalRandom.current().nextInt(lifeFormsKeys.size());
            Set<String> selectedTypes = getRandomTypes(lifeFormsKeys, count);

            for (String type : selectedTypes) {
                LifeForm prototype = lifeFormsMap.get(type);
                int amount = 1 + ThreadLocalRandom.current().nextInt(prototype.getMaxPerCell());

                for (int i = 0; i < amount; i++) {
                    LifeForm instance = cloneLifeForm(prototype);
                    cell.addLifeForm(instance);
                }
            }
        }
    }

    private LifeForm cloneLifeForm(LifeForm original) {
        return original.clone();
    }

    private List<Point> getRandomCoordinates(GameMap gameMap) {
        GameMapConfig settings = ConfigManager.getSettings().getGameMapConfig();
        int totalCell = gameMap.getWidth() * gameMap.getHeight();
        double initialFillPercentage = settings.getInitialFillPercentage();
        int cellsToFill = (int) Math.ceil(totalCell * initialFillPercentage);

        Set<Point> randomCoordinates = new HashSet<>();

        while (randomCoordinates.size() < cellsToFill) {
            int x = ThreadLocalRandom.current().nextInt(gameMap.getWidth());
            int y = ThreadLocalRandom.current().nextInt(gameMap.getHeight());
            randomCoordinates.add(new Point(x, y));
        }
        return new ArrayList<>(randomCoordinates);
    }

    private Map<String, LifeForm> initializeLifeFormsMap() {
        Map<String, LifeFormConfig> settingsMap = ConfigManager.getSettings().getLifeForms();
        Map<String, LifeForm> lifeFormsMap = new HashMap<>();

        for (Class<?> clazz : LIFE_FORM_CLASS_TYPES) {
            LifeForm lifeForm = createLifeForm(clazz, settingsMap);
            lifeFormsMap.put(lifeForm.getLabel(), lifeForm);
        }
        return lifeFormsMap;
    }

    private LifeForm createLifeForm(Class<?> clazz, Map<String, LifeFormConfig> settingsMap) {
        String key = clazz.getSimpleName().toLowerCase();
        LifeFormConfig config = settingsMap.get(key);

        if (config == null) {
            throw new GameInitializationException("Нет конфигурации для LifeForm: " + key);
        }

        try {
            return getLifeForm(clazz, config);
        } catch (Exception e) {
            throw new GameInitializationException("Ошибка создания LifeForm: " + key, e);
        }
    }

    private LifeForm getLifeForm(Class<?> clazz, LifeFormConfig config)
            throws Exception {

        Constructor<?> constructor = clazz.getDeclaredConstructor(
                String.class, String.class, Double.class, Integer.class, Integer.class, Double.class, String.class
        );
        constructor.setAccessible(true);

        LifeForm instance = (LifeForm) constructor.newInstance(
                config.getLabel(),
                config.getName(),
                config.getWeight(),
                config.getMaxPerCell(),
                config.getMaxSpeed(),
                config.getFoodToSaturate(),
                config.getImage()
        );

        if (instance instanceof Animal animal && config.getFoodPreferences() != null) {
            for (Map.Entry<String, Integer> entry : config.getFoodPreferences().entrySet()) {
                String className = capitalize(entry.getKey());
                animal.getFoodPreferences().put(className, entry.getValue());
            }
        }

        return instance;
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private Set<String> getRandomTypes(List<String> types, int count) {
        Collections.shuffle(types);
        return new HashSet<>(types.subList(0, count));
    }
}
