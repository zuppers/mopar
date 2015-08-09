package io.mopar.game.model;

import io.mopar.game.config.VariableConfig;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Hadyn Fitzgerald
 */
public class VariableSet {

    /**
     * The variable values.
     */
    private Map<Integer, Integer> values = new TreeMap<>();

    /**
     * The updated variables.
     */
    private Set<Integer> updated = new HashSet<>();

    /**
     * Sets a variable.
     *
     * @param id The variable id.
     * @param value The variable value.
     */
    public void setValue(int id, int value) {
        values.put(id, value);
        updated.add(id);
    }

    /**
     * Gets if a variable is registered.
     *
     * @param id The variable id.
     * @return If the specified variable is registered.
     */
    public boolean contains(int id) {
        return values.containsKey(id);
    }

    /**
     * Gets a variable.
     *
     * @param id The variable id.
     * @return The value or if there is no value registered for the specified id; 0.
     */
    public int getValue(int id) {
        if(!contains(id)) {
            VariableConfig config = VariableConfig.forId(id);
            if(config == null) {
                return 0;
            }
            return config.getDefaultValue();
        }
        return values.get(id);
    }

    /**
     * Gets the entries.
     *
     * @return the entries.
     */
    public Set<Map.Entry<Integer, Integer>> getEntries() {
        return values.entrySet();
    }

    /**
     * Gets the updated variable ids.
     *
     * @return the updated variables.
     */
    public Set<Integer> getUpdated() {
        return updated;
    }
}
