package net.objectof.actof.minion;


import java.util.HashMap;
import java.util.Map;


public class Settings {

    private static Map<String, Object> settings = new HashMap<>();

    public static <T> T get(String key, T def) {
        if (!settings.containsKey(key)) { return def; }
        return ((T) settings.get(key));
    }

    public static void put(String key, Object value) {
        settings.put(key, value);
    }
}
