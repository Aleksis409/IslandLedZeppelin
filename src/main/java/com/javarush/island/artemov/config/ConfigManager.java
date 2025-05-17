package com.javarush.island.artemov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.javarush.island.artemov.exception.ConfigurationLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ConfigManager {
    private static GameSettings instance;

    private ConfigManager() {
    }

    public static void loadSettings() {
        if (instance == null) {
            String path = Default.SETTING_YAML_FILE_PATH;
            URL resource = ConfigManager.class.getClassLoader().getResource(path);

            if (resource == null) {
                throw new IllegalArgumentException("Файл настроек не найден по пути: " + path);
            }

            try (InputStream is = resource.openStream()) {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                instance = mapper.readValue(is, GameSettings.class);
            } catch (IOException e) {
                throw new ConfigurationLoadException("Не удалось загрузить файл настроек: " + path, e);
            }
        }
    }

    public static GameSettings getSettings() {
        if (instance == null) {
            throw new IllegalStateException("Настройки не были загружены.");
        }
        return instance;
    }
}
