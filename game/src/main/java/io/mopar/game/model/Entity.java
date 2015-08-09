package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public abstract class Entity {

    /**
     * The id.
     */
    private int id = -1;

    /**
     * The position.
     */
    private Position position = new Position(3222, 3222);

    /**
     * Updated flag.
     */
    private boolean updated;

    /**
     * The variables.
     */
    private VariableSet variables = new VariableSet();

    /**
     * Constructs a new {@link Entity};
     */
    protected Entity() {}

    /**
     * Sets the id.
     *
     * @param id The id.
     */
    void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public boolean isRemoved() {
        return id == -1;
    }

    /**
     * Sets the position.
     *
     * @param position The position.
     */
    public void setPosition(Position position) {
        if(position == null) {
            throw new NullPointerException("Position cannot be null");
        }
        this.position = position;
    }

    /**
     * Gets the position.
     *
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the value of a variable.
     *
     * @param id the variable id.
     * @param value the value.
     */
    public void setVariable(int id, int value) {
        variables.setValue(id, value);
    }

    /**
     * Gets the value of a variable.
     *
     * @param id the variable id.
     * @return the value.
     */
    public int getVariable(int id) {
        return variables.getValue(id);
    }

    /**
     *
     * @param id
     * @param off
     * @param len
     * @param value
     */
    public void setBitVariable(int id, int off, int len, int value) {
        int mask = (1 << len) - 1;
        int i = (value & mask) << off;
        setVariable(id, (getVariable(id) & ~(mask << off)) | i);
    }

    /**
     *
     * @param id
     * @param off
     * @param len
     * @return
     */
    public int getBitVariable(int id, int off, int len) {
        int mask = (1 << len) - 1;
        return getVariable(id) >>> off & mask;
    }

    /**
     *
     * @return
     */
    public VariableSet getVariables() {
        return variables;
    }
}