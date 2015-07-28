package io.mopar.account;

/**
 * Created by hadyn on 7/28/2015.
 */
public class SkillModel {
    private int id;
    private double experience;
    private int stat;

    public SkillModel() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }
}
