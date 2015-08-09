package io.mopar.game.config;

/**
 * @author Hadyn Fitzgerald
 */
public class InterfaceComponent {

    /**
     * The parent id.
     */
    private int parentId;

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    /**
     * The child component actions.
     */
    private ChildActions childActions;

    /**
     * Constructs a new {@link InterfaceComponent};
     */
    public InterfaceComponent() {}

    /**
     * Sets the parent id.
     *
     * @param parentId the parent id.
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    /**
     * Gets the parent id.
     *
     * @return the parent id.
     */
    public int getParentId() {
        return parentId;
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
     * Gets the child actions.
     *
     * @return the actions.
     */
    public ChildActions getChildActions() {
        return childActions;
    }
}
