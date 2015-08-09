package io.mopar.game.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

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
public class ItemConfig {

    /**
     * The configuration list type token.
     */
    private static final TypeToken<List<ItemConfig>> LIST_TYPE_TOKEN = new TypeToken<List<ItemConfig>>() {};

    /**
     * The configurations mapped from their id.
     */
    private static Map<Integer, ItemConfig> configurations = new HashMap<>();

    /**
     * The configurations mapped from their name.
     */
    private static Map<String, ItemConfig> configurationsByName = new HashMap<>();

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     * Flag for if the item naturally stacks.
     */
    private boolean stackable;

    /**
     * Constructs a new {@link ItemConfig};
     */
    public ItemConfig() {}

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
        List<ItemConfig> configs = new Gson().fromJson(reader, LIST_TYPE_TOKEN.getType());
        configs.forEach(config -> append(config));
    }

    /**
     * Appends a configuration.
     *
     * @param config the configuration.
     */
    public static void append(ItemConfig config) {
        configurations.put(config.getId(), config);
        configurationsByName.put(config.getName(), config);
    }

    /**
     * Gets a configuration for its id.
     *
     * @param id the id of the configuration.
     * @return the configuration or a default configuration if there is no registered configuration under the provided id.
     */
    public static ItemConfig forId(int id) {
        if(!configurations.containsKey(id)) {
            ItemConfig config = new ItemConfig();
            config.setId(id);
            config.setName("item" + id);
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
    public static ItemConfig forName(String name) {
        if(!configurationsByName.containsKey(name)) {
            return null;
        }
        return configurationsByName.get(name);
    }
}