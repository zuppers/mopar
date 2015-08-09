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
public class VarbitConfig {

    /**
     * The configuration list type token.
     */
    private static final TypeToken<List<VarbitConfig>> LIST_TYPE_TOKEN = new TypeToken<List<VarbitConfig>>() {};

    /**
     * The configurations mapped from their id.
     */
    private static Map<Integer, VarbitConfig> configurations = new HashMap<>();

    /**
     * The configurations mapped from their name.
     */
    private static Map<String, VarbitConfig> configurationsByName = new HashMap<>();

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     * The Varbit id.
     */
    private int variableId;

    /**
     * The bit offset in the Varbit.
     */
    private int offset;

    /**
     * The length of the Varbit in bits.
     */
    private int length;

    /**
     * Constructs a new {@link VarbitConfig};
     */
    public VarbitConfig() {}

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
     * Sets the variable id.
     *
     * @param variableId the variable id.
     */
    public void setVariableId(int variableId) {
        this.variableId = variableId;
    }

    /**
     * Gets the variable id.
     *
     * @return the variable id.
     */
    public int getVariableId() {
        return variableId;
    }

    /**
     * Sets the offset.
     *
     * @param offset the offset.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Gets the offset.
     *
     * @return the offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the length.
     *
     * @param length the length.
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Gets the length.
     *
     * @return the length.
     */
    public int getLength() {
        return length;
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
        List<VarbitConfig> configs = new Gson().fromJson(reader, LIST_TYPE_TOKEN.getType());
        configs.forEach(config -> append(config));
    }

    /**
     * Appends a configuration.
     *
     * @param config the configuration.
     */
    public static void append(VarbitConfig config) {
        configurations.put(config.getId(), config);
        configurationsByName.put(config.getName(), config);
    }

    /**
     * Gets a configuration for its id.
     *
     * @param id the id of the configuration.
     * @return the configuration or {@code null} if there is no registered configuration under the provided id.
     */
    public static VarbitConfig forId(int id) {
        if(!configurations.containsKey(id)) {
            return null;
        }
        return configurations.get(id);
    }

    /**
     * Gets an configuration for its name.
     *
     * @param name the name of the configuration.
     * @return the configuration or {@code null} if there is no registered configuration under the provided name.
     */
    public static VarbitConfig forName(String name) {
        if(!configurationsByName.containsKey(name)) {
            return null;
        }
        return configurationsByName.get(name);
    }
}
