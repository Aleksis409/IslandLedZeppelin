package com.javarush.island.artemov.service;

import com.javarush.island.artemov.config.*;
import com.javarush.island.artemov.entity.lifeforms.LifeForm;
import com.javarush.island.artemov.entity.map.GameMap;
import com.javarush.island.artemov.entity.map.Location;
import com.javarush.island.artemov.util.RandomSelection;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

public class GameInitializer {

    public static GameMap initialGameMap() {
        GameMap gameMap = createEmptyGameMapFromYaml();
        generateInitialMapState(gameMap);
        return gameMap;
    }

    private static GameMap createEmptyGameMapFromYaml() {
        GameMapConfig settings = ConfigManager.getSettings().getGameMap();
        int width = settings.getWidth();
        int height = settings.getHeight();
        return new GameMap(width, height);
    }

    private static void generateInitialMapState(GameMap gameMap) {
        Map<String, LifeForm> lifeFormMap = initializeLifeFormsMap();
        List<String> keysLifeFormMap = new ArrayList<>(lifeFormMap.keySet());
        Random random = new Random();

        List<Location> randomCells = getRandomCell(gameMap);
        for (Location randomCell : randomCells) {
            int randomLifeFormCount = random.nextInt(lifeFormMap.size()) + 1;
            List<Integer> lifeFormsNum = RandomSelection.getRandomNumbers(keysLifeFormMap.size(), randomLifeFormCount);
            for (int i = 0; i < lifeFormsNum.size(); i++) {
                randomCell.addLifeForfm(lifeFormMap.get(keysLifeFormMap.get(lifeFormsNum.get(i))));
            }
        }
    }

    private static List<Location> getRandomCell(GameMap gameMap) {
        List<Location> randomCells = new ArrayList<>();
        GameMapConfig settings = ConfigManager.getSettings().getGameMap();
        double initialFillPercentage = settings.getInitialFillPercentage();
        int cellsToFill = (int) Math.ceil(gameMap.getWidth() * gameMap.getHeight() * initialFillPercentage);
        List<Integer> xCoordinates = RandomSelection.getRandomNumbers(gameMap.getWidth(), cellsToFill);
        List<Integer> yCoordinates = RandomSelection.getRandomNumbers(gameMap.getHeight(), cellsToFill);
        for (int i = 0; i < cellsToFill; i++) {
            randomCells.add(gameMap.getLocation(xCoordinates.get(i), yCoordinates.get(i)));
        }
        return randomCells;
    }

    private static Map<String, LifeForm> initializeLifeFormsMap() {
        Map<String, LifeFormConfig> settingsMap = ConfigManager.getSettings().getLifeForms();
        Map<String, LifeForm> lifeFormMap = new HashMap<>();

        for (Class<?> clazz : Default.LIFE_FORM_CLASS_TYPES) {
            createLifeForm(clazz, settingsMap).ifPresent(lifeForm ->
                    lifeFormMap.put(lifeForm.getName(), lifeForm)
            );
        }
        return lifeFormMap;
    }

    private static Optional<LifeForm> createLifeForm(Class<?> clazz, Map<String, LifeFormConfig> settingsMap) {
        try {
            String key = clazz.getSimpleName().toLowerCase();
            LifeFormConfig config = settingsMap.get(key);

            if (config == null) {
                System.err.println("⚠️ Нет конфигурации для класса: " + key);
                return Optional.empty();
            }

            Constructor<?> constructor = clazz.getDeclaredConstructor(
                    String.class, Double.class, Integer.class, Integer.class, Double.class, String.class
            );
            constructor.setAccessible(true);
            LifeForm instance = (LifeForm) constructor.newInstance(
                    config.getName(),
                    config.getWeight(),
                    config.getMaxPerCell(),
                    config.getMaxSpeed(),
                    config.getFoodToSaturate(),
                    config.getImage()
            );
            return Optional.of(instance);
        } catch (Exception e) {
            System.err.println("Ошибка создания LifeForm для класса: " + clazz.getSimpleName());
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
