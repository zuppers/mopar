package io.mopar.account;

/**
 * @author Hadyn Fitzgerald
 */
public class VariableModel {

    /**
     * The id.
     */
    private int id;

    /**
     * The value.
     */
    private int value;

    /**
     * Constructs a new {@link VariableModel};
     */
    public VariableModel() {}


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}