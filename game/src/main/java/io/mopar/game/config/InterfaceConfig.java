package io.mopar.game.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hadyn Fitzgerald
 */
public class InterfaceConfig {

    /**
     * The interface config list type token.
     */
    private static final TypeToken<List<InterfaceConfig>> LIST_TYPE_TOKEN = new TypeToken<List<InterfaceConfig>>() {};

    /**
     * The configurations mapped by their id.
     */
    private static Map<Integer, InterfaceConfig> configurations = new HashMap<>();

    /**
     * The configurations mapped by their name.
     */
    private static Map<String, InterfaceConfig> configurationsByName = new HashMap<>();

    /**
     * The id.
     */
    private int id;

    /**
     * The name of the interface.
     */
    private String name;

    /**
     * The components.
     */
    private List<InterfaceComponent> components = new ArrayList<>();

    /**
     * Constructs a new {@link InterfaceConfig};
     */
    public InterfaceConfig() {}

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
     * Adds a component.
     *
     * @param component the component to add.
     */
    public void addComponent(InterfaceComponent component) {
        components.add(component);
    }

    /**
     *
     * @param componentId
     * @return
     */
    public InterfaceComponent getComponent(int componentId) {
        for(InterfaceComponent component : components) {
            if(component.getId() != componentId) {
                continue;
            }
            return component;
        }
        return null;
    }

    /**
     * Gets the components.
     *
     * @return the components.
     */
    public List<InterfaceComponent> getComponents() {
        return components;
    }

    /**
     * Gets a component for its name.
     *
     * @param name the component name.
     * @return the component.
     */
    public InterfaceComponent getComponent(String name) {
        for(InterfaceComponent component : components) {
            if(name.equals(component.getName())) {
                return component;
            }
        }
        return null;
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
        List<InterfaceConfig> configs = new Gson().fromJson(reader, LIST_TYPE_TOKEN.getType());
        configs.forEach(config -> append(config));
    }

    /**
     * Appends a configuration.
     *
     * @param config the configuration.
     */
    public static void append(InterfaceConfig config) {
        for(InterfaceComponent component : config.getComponents()) {
            component.setParentId(config.getId());
        }

        configurations.put(config.getId(), config);
        configurationsByName.put(config.getName(), config);
    }

    /**
     * Gets a configuration for its id.
     *
     * @param id the id of the configuration.
     * @return the configuration.
     */
    public static InterfaceConfig forId(int id) {
        if(!configurations.containsKey(id)) {
            return null;
        }
        return configurations.get(id);
    }

    public static InterfaceComponent getComponent(int interfaceId, int componentId) {
        InterfaceConfig config = forId(interfaceId);
        if(config != null) {
            config.getComponent(componentId);
        }
        return null;
    }

    /**
     * Gets a configuration for its name.
     *
     * @param name the name of the configuration.
     * @return the configuration.
     */
    public static InterfaceConfig forName(String name) {
        if(!configurationsByName.containsKey(name)) {
            return null;
        }
        return configurationsByName.get(name);
    }
}
