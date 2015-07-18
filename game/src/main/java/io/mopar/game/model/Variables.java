package io.mopar.game.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Hadyn Fitzgerald
 */
public class Variables {

    /**
     * The variable values.
     */
    private Map<Integer, Integer> values = new TreeMap<>();

    /**
     * Sets a variable.
     *
     * @param id The variable id.
     * @param value The variable value.
     */
    public void setValue(int id, int value) {
        values.put(id, value);
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
            return 0;
        }
        return values.get(id);
    }
}
