package io.mopar.game.config;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Hadyn Fitzgerald
 */
public class ChildActions {

    /**
     * The start id.
     */
    private int start;

    /**
     * The end id.
     */
    private int end;

    /**
     * The actions.
     */
    private Set<ActionAccess> actions = new HashSet<>();

    /**
     * Constructs a new {@link ChildActions};
     */
    public ChildActions() {}

    /**
     * Sets the start.
     *
     * @param start the start.
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * Gets the start.
     *
     * @return the start.
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the end.
     *
     * @param end the end.
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Gets the end.
     *
     * @return the end.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Adds access for an action.
     *
     * @param action the action.
     */
    public void addActionAccess(ActionAccess action) {
        actions.add(action);
    }

    /**
     * Gets the actions.
     *
     * @return the actions.
     */
    public Set<ActionAccess> getActions() {
        return actions;
    }

    /**
     *
     * @return
     */
    public int getFlags() {
        return actions.stream().mapToInt(action -> action.getFlag()).sum();
    }
}
