package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private Properties properties;

    public ConfigReader() {

        properties = new Properties();

        try (InputStream input =
                getClass()
                .getClassLoader()
                .getResourceAsStream(
                        "config/config.properties"
                )) {

            if (input == null) {
                throw new RuntimeException(
                        "config.properties not found"
                );
            }

            properties.load(input);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load config.properties",
                    e
            );
        }
    }

    public String getProperty(String key) {

        return properties.getProperty(key);
    }
}