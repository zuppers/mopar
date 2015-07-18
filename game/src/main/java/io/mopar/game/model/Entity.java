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
    private Position position = new Position();

    /**
     * Updated flag.
     */
    private boolean updated;

    /**
     * The variables.
     */
    private Variables variables = new Variables();

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
    public void setPosition(Position position) { this.position = position; }

    /**
     * Gets the position.
     *
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets if the entity has been updated.
     *
     * @param updated The updated flag.
     */
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    /**
     * Gets if the entity has been updated.
     *
     * @return If the entity was updated.
     */
    public boolean isUpdated() {
        return updated;
    }
}