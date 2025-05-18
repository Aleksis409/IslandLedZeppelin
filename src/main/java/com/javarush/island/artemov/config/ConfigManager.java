package com.javarush.island.artemov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;

public class ConfigManager {
    private static GameSettings instance;

    private ConfigManager() {}

    public static void loadSettings() throws IOException {
        if (instance == null) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            InputStream is = ConfigManager.class.getClassLoader()
                    .getResourceAsStream(Default.SETTING_YAML_FILE_PATH);
            if (is == null) {
                throw new IllegalArgumentException("Не найден файл настроек");
            }
            instance = mapper.readValue(is, GameSettings.class);
        }
    }

    public static GameSettings getSettings() {
        if (instance == null) {
            throw new IllegalStateException("Настройки не были загружены.");
        }
        return instance;
    }
}
