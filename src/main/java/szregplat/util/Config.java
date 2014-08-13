package szregplat.util;

import szregplat.omc.Ws320RuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Administrator on 14-4-13.
 */
public class Config {
    private static final Map<String, Map<String, String>> CONFIGS = new HashMap<String, Map<String, String>>();
    private Map<String, String> config;

    private Config(String file) {
        this.config = CONFIGS.get(file);
    }

    public static Config getInstance(String file) {
        if (!CONFIGS.containsKey(file)) {
            Map<String, String> conf = load(file);
            CONFIGS.put(file, conf);
        }

        return new Config(file);
    }

    private static Map<String, String> load(String file) {
        Properties properties = new Properties();
        InputStream is = Config.class.getClassLoader().getResourceAsStream(file);
        if (is == null) is = Config.class.getResourceAsStream(file);

        try {
            properties.load(is);
        } catch (IOException e) {
            throw new Ws320RuntimeException(e);
        }

        Map<String, String> map = new HashMap<String, String>();

        Set<String> names = properties.stringPropertyNames();
        for (String name : names) {
            String value = properties.getProperty(name);
            map.put(name, value);
        }

        return map;
    }

    public String getString(String key) {
        return this.config.get(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(getString(key));
    }

    public Set<String> getKeys() {
        return this.config.keySet();
    }

}
