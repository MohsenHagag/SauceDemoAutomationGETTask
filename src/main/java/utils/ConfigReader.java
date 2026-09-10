package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String override = System.getProperty(key);
        if (override != null && !override.isBlank()) {
            return override;
        }
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        return value != null ? Integer.parseInt(value.trim()) : defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value != null ? Boolean.parseBoolean(value.trim()) : defaultValue;
    }

    public static String browser() {
        return get("browser", "chrome");
    }

    public static String baseUrl() {
        return get("url", "https://www.saucedemo.com/");
    }

    public static boolean headless() {
        return getBoolean("headless", false);
    }

    public static String validUsername() {
        return get("valid.username", "standard_user");
    }

    public static String validPassword() {
        return get("valid.password", "secret_sauce");
    }
}