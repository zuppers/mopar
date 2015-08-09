package io.mopar.game.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SongConfig {

    /**
     * The logger.
     */
    private static final Logger logger = LoggerFactory.getLogger(SongConfig.class);

    /**
     * The class config type token.
     */
    private static final TypeToken<List<SongConfig>> LIST_TYPE_TOKEN = new TypeToken<List<SongConfig>>() {};

    /**
     * The configurations mapped from their id.
     */
    private static Map<Integer, SongConfig> configurations = new HashMap<>();

    /**
     * The configurations mapped from their name.
     */
    private static Map<String, SongConfig> configurationsByName = new HashMap<>();

    /**
     * The configurations mapped from their region.
     */
    private static Map<Integer, SongConfig> configurationsByRegion = new HashMap<>();

    static class Region {

        /**
         * The x coordinate.
         */
        int x;

        /**
         * The y coordinate.
         */
        int y;

        /**
         * Constructs a new {@link Region};
         */
        public Region() {}

        /**
         * Constructs a new {@link Region};
         *
         * @param x
         * @param y
         */
        public Region(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Sets the x coordinate.
         *
         * @param x the x coordinate.
         */
        public void setX(int x) {
            this.x = x;
        }

        /**
         * Sets the y coordinate.
         *
         * @param y the y coordinate.
         */
        public void setY(int y) {
            this.y = y;
        }

        /**
         * Gets the region as an integer.
         *
         * @return the integer value.
         */
        public int code() {
            return codeOf(x, y);
        }

        /**
         * Gets the hash value of the provided region coordinates.
         *
         * @param x the x coordinate.
         * @param y the y coordinate.
         * @return the hash of the coordinates.
         */
        public static int codeOf(int x, int y) {
            return x << 8 | y;
        }
    }

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     * The formatted name.
     */
    private String formattedName;

    /**
     * The regions.
     */
    private List<Region> regions = new ArrayList<>();

    /**
     * The file id for the song.
     */
    private int fileId;

    /**
     * Constructs a new {@link SongConfig};
     */
    public SongConfig() {}

    /**
     * Constructs a new {@link SongConfig};
     *
     * @param id the id.
     * @param name the name of the song.
     */
    public SongConfig(int id, String name) {
        this.id = id;
        this.name = name;
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
     * Sets the formatted name.
     *
     * @param formattedName the formatted name.
     */
    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    /**
     * Gets the formatted name.
     *
     * @return the formatted name.
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     * Sets the file id.
     *
     * @param fileId the file id.
     */
    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    /**
     * Gets the file id.
     *
     * @return the file id.
     */
    public int getFileId() {
        return fileId;
    }

    /**
     * Gets the regions.
     *
     * @return the regions.
     */
    public List<Region> getRegions() {
        return regions;
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
        List<SongConfig> configs = new Gson().fromJson(reader, LIST_TYPE_TOKEN.getType());
        configs.forEach(config -> append(config));
    }

    /**
     * Appends a configuration.
     *
     * @param config the configuration.
     */
    public static void append(SongConfig config) {
        configurations.put(config.getId(), config);
        configurationsByName.put(config.getName(), config);
        for(Region region : config.getRegions()) {
            configurationsByRegion.put(region.code(), config);
        }
    }

    /**
     * Gets a configuration for its id.
     *
     * @param id the id of the configuration.
     * @return the configuration or {@code null} if there is no registered configuration under the provided id.
     */
    public static SongConfig forId(int id) {
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
    public static SongConfig forName(String name) {
        if(!configurationsByName.containsKey(name)) {
            return null;
        }
        return configurationsByName.get(name);
    }

    /**
     * Gets a configuration for the provided coordinates.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return the configuration or {@code null} if there is no registered configuration under the provided region.
     */
    public static SongConfig forCoordinates(int x, int y) {
        return ForRegion(x >> 6, y >> 6);
    }

    /**
     * Gets a configuration for the provided region.
     *
     * @param x the region x coordinate.
     * @param y the region y coordinate.
     * @return the configuration or {@code null} if there is no registered configuration under the provided region.
     */
    public static SongConfig ForRegion(int x, int y) {
        int code = Region.codeOf(x, y);
        if(!configurationsByRegion.containsKey(code)) {
            return null;
        }
        return configurationsByRegion.get(code);
    }
}
