package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class NPC extends Mobile {

    /**
     * The type of the npc.
     */
    private int type;

    /**
     *
     */
    public NPC(int type) {
        this.type = type;
    }

    /**
     *
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets the type.
     *
     * @return The type.
     */
    public int getType() {
        return type;
    }

}
