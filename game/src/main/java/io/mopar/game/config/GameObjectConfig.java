package io.mopar.game.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author Hadyn Fitzgerald
 */
public class GameObjectConfig {

    /**
     * The configuration list type token.
     */
    private static final TypeToken<List<GameObjectConfig>> LIST_TYPE_TOKEN = new TypeToken<List<GameObjectConfig>>() {};

    /**
     * The configurations mapped from their id.
     */
    private static Map<Integer, GameObjectConfig> configurations = new HashMap<>();

    /**
     * The configurations mapped from their name.
     */
    private static Map<String, GameObjectConfig> configurationsByName = new HashMap<>();

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     *
     */
    private String formattedName;

    /**
     * The width.
     */
    private int width = 1;

    /**
     * The height.
     */
    private int height = 1;

    /**
     *
     */
    private boolean solid = true;

    /**
     *
     */
    private boolean impenetrable = true;

    /**
     * Constructs a new {@link GameObjectConfig};
     */
    public GameObjectConfig() {}

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
     *
     * @param formattedName
     */
    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    /**
     *
     * @return
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Gets the width.
     *
     * @return the width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height.
     *
     * @return the height.
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     * @return
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     *
     * @return
     */
    public boolean isImpenetrable() {
        return impenetrable;
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
        List<GameObjectConfig> configs = new Gson().fromJson(reader, LIST_TYPE_TOKEN.getType());
        configs.forEach(config -> append(config));
    }

    /**
     * Appends a configuration.
     *
     * @param config the configuration.
     */
    public static void append(GameObjectConfig config) {
        configurations.put(config.getId(), config);
        configurationsByName.put(config.getName(), config);
    }

    /**
     * Gets a configuration for its id.
     *
     * @param id the id of the configuration.
     * @return the configuration or a default configuration if there is no registered configuration under the provided id.
     */
    public static GameObjectConfig forId(int id) {
        if(!configurations.containsKey(id)) {
            GameObjectConfig config = new GameObjectConfig();
            config.setId(id);
            config.setName("Object" + id);
            config.setFormattedName("Object" + id);
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
    public static GameObjectConfig forName(String name) {
        if(!configurationsByName.containsKey(name)) {
            return null;
        }
        return configurationsByName.get(name);
    }
}
