package com.javarush.island.artemov.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.javarush.island.artemov.config.Default;
import com.javarush.island.artemov.config.GameMapConfig;
import com.javarush.island.artemov.config.GameSettings;
import com.javarush.island.artemov.config.LifeFormConfig;
import com.javarush.island.artemov.entity.lifeforms.LifeForfm;
import com.javarush.island.artemov.entity.map.GameMap;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameInitializer {


    public static GameMap initializeGameMapFromYaml() throws Exception {
        GameMapConfig settings = getFullSettings().gameMap;
        int width = settings.getWidth();
        int height = settings.getHeight();
        return new GameMap(width, height);
    }

    public static List<LifeForfm> initializeLifeForms() throws Exception {
        List<LifeForfm> lifeForms = new ArrayList<>();
        Map<String, LifeFormConfig> settingsMap = getFullSettings().lifeForms;

        for (Class<?> lifeFormClass : Default.LIFE_FORM_CLASS_TYPES) {
            String key = lifeFormClass.getSimpleName().toLowerCase();
            LifeFormConfig config = settingsMap.get(key);
            if (config == null) {
                System.err.println("Нет конфигурации для " + key);
                continue;
            }

            Constructor<?> constructor = lifeFormClass.getDeclaredConstructor(
                    String.class, Double.class, Integer.class, Integer.class, Double.class, String.class
            );
            constructor.setAccessible(true);
            LifeForfm instance = (LifeForfm) constructor.newInstance(
                    config.getName(),
                    config.getWeight(),
                    config.getMaxPerCell(),
                    config.getMaxSpeed(),
                    config.getFoodToSaturate(),
                    config.getImage()
            );
            lifeForms.add(instance);
        }

        return lifeForms;
    }

    private static GameSettings getFullSettings() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream is = GameInitializer.class.getClassLoader()
                .getResourceAsStream(Default.SETTING_YAML_FILE_PATH);
        if (is == null) {
            throw new IllegalArgumentException("Не найден файл настроек");
        }

        return mapper.readValue(is, GameSettings.class);
    }

}
