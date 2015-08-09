package io.mopar.game.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class InventoryConfig {

    /**
     * The inventory config type token.
     */
    private static final TypeToken<List<InventoryConfig>> LIST_TYPE_TOKEN = new TypeToken<List<InventoryConfig>>() {};

    /**
     * The configurations mapped from their id.
     */
    private static Map<Integer, InventoryConfig> configurations = new HashMap<>();

    /**
     * The configurations mapped from their name.
     */
    private static Map<String, InventoryConfig> configurationsByName = new HashMap<>();

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     * The size.
     */
    private int size;

    /**
     * Constructs a new {@link InventoryConfig};
     */
    public InventoryConfig() {}

    /**
     * Constructs a new {@link InventoryConfig};
     *
     * @param id the id.
     * @param name the name of the inventory.
     * @param size the size of the inventory.
     */
    public InventoryConfig(int id, String name, int size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    /**
     * Sets the id.
     *
     * @param id the id.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the id.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the name.
     *
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the size.
     *
     * @param size the size.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets the size.
     *
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Parses a JSON formatted string to append configurations.
     *
     * @param json the json string.
     */
    public static void parse(String json) {
        parse(new StringReader(json));
    }

    /**
     * Parses an input stream. The inputted format is expected to be JSON.
     *
     * @param is the input stream to parse.
     */
    public static void parse(InputStream is) {
        parse(new InputStreamReader(is));
    }

    /**
     * Parses a JSON formatted string to append configurations.
     *
     * @param reader the reader to parse.
     */
    public static void parse(Reader reader) {
        List<InventoryConfig> configs = new Gson().fromJson(reader, LIST_TYPE_TOKEN.getType());
        configs.forEach(config -> append(config));
    }

    /**
     * Appends a configuration.
     *
     * @param config the configuration.
     */
    public static void append(InventoryConfig config) {
        configurations.put(config.getId(), config);
        configurationsByName.put(config.getName(), config);
    }

    /**
     * Gets a configuration for its id.
     *
     * @param id the id of the configuration.
     * @return the configuration or a default configuration if there is no registered configuration under the provided id.
     */
    public static InventoryConfig forId(int id) {
        if(!configurations.containsKey(id)) {
            InventoryConfig config = new InventoryConfig();
            config.setId(id);
            config.setName("inventory" + id);
            config.setSize(0);
            return config;
        }
        return configurations.get(id);
    }

    /**
     * Gets an configuration for its name.
     *
     * @param name the name of the configuration.
     * @return the configuration or {@code null} if there is no registered configuration under the provided name.
     */
    public static InventoryConfig forName(String name) {
        if(!configurationsByName.containsKey(name)) {
            return null;
        }
        return configurationsByName.get(name);
    }
}
