package io.mopar.account;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hadyn Fitzgerald
 */
public class Profile {

    /**
     * The user id.
     */
    private long uid;

    /**
     * The x coordinate.
     */
    private int x;

    /**
     * The y coordinate.
     */
    private int y;

    /**
     * The plane coordinate.
     */
    private int plane;

    /**
     * The skill models.
     */
    private List<SkillModel> skills = new ArrayList<>();

    /**
     * The inventory models.
     */
    private List<InventoryModel> inventories = new ArrayList<>();

    /**
     * The variable models.
     */
    private List<VariableModel> variables = new ArrayList<>();

    /**
     * Constructs a new {@link Profile};
     */
    public Profile() {}


    /**
     * Sets the unique user id.
     *
     * @param uid the user id.
     */
    public void setUid(long uid) {
        this.uid = uid;
    }

    /**
     * Gets the user id.
     *
     * @return the user id.
     */
    public long getUid() {
        return uid;
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
     *
     * @return
     */
    public int getX() {
        return x;
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
     *
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the plane.
     *
     * @param plane the plane.
     */
    public void setPlane(int plane) {
        this.plane = plane;
    }

    /**
     *
     * @return
     */
    public int getPlane() {
        return plane;
    }

    /**
     *
     * @param skill
     */
    public void addSkill(SkillModel skill) {
        skills.add(skill);
    }

    /**
     *
     * @return
     */
    public List<SkillModel> getSkills() {
        return skills;
    }

    /**
     *
     * @param inventory
     */
    public void addInventory(InventoryModel inventory) {
        inventories.add(inventory);
    }

    /**
     *
     * @return
     */
    public List<InventoryModel> getInventories() {
        return inventories;
    }

    /**
     *
     * @param variable
     */
    public void addVariable(VariableModel variable) {
        variables.add(variable);
    }

    /**
     *
     * @return
     */
    public List<VariableModel> getVariables() {
        return variables;
    }
}