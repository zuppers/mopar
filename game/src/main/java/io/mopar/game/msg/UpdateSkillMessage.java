package io.mopar.game.msg;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class UpdateSkillMessage extends Message {
    private int id;
    private int experience;
    private int stat;

    public UpdateSkillMessage(int id, int experience, int stat) {
        this.id = id;
        this.experience = experience;
        this.stat = stat;
    }

    public int getId() {
        return id;
    }

    public int getExperience() {
        return experience;
    }

    public int getStat() {
        return stat;
    }
}
