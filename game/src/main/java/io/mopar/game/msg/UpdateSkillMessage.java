package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class UpdateSkillMessage extends Message {

    /**
     *
     */
    private int id;

    /**
     *
     */
    private int experience;

    /**
     *
     */
    private int stat;

    /**
     *
     * @param id
     * @param experience
     * @param stat
     */
    public UpdateSkillMessage(int id, int experience, int stat) {
        this.id = id;
        this.experience = experience;
        this.stat = stat;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public int getExperience() {
        return experience;
    }

    /**
     *
     * @return
     */
    public int getStat() {
        return stat;
    }
}
