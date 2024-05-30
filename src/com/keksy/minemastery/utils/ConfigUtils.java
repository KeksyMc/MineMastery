package com.keksy.minemastery.utils;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ConfigUtils {

    private final Yaml yaml;
    private final File configFile;

    public ConfigUtils(File configFile) {
        this.configFile = configFile;
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        this.yaml = new Yaml(new Constructor(), representer, options);
    }

    public Map<String, Object> loadConfig() throws IOException {
        try (InputStream inputStream = new FileInputStream(configFile)) {
            return yaml.load(inputStream);
        }
    }

    public void saveConfig(Map<String, Object> data) throws IOException {
        try (FileWriter writer = new FileWriter(configFile)) {
            yaml.dump(data, writer);
        }
    }
}
