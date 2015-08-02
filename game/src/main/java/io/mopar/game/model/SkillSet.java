package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class SkillSet {

    /**
     *
     */
    private int[] stats = new int[Skills.COUNT];

    /**
     *
     */
    private double[] experience = new double[Skills.COUNT];

    /**
     *
     */
    public SkillSet() {
        for(int i = 0; i < stats.length; i++) {
            stats[i] = 1;
            experience[i] = 0.0D;
        }
        stats[Skills.HITPOINTS] = 10;
        experience[Skills.HITPOINTS] = 1184;
    }

    /**
     *
     * @param skill
     * @param stat
     */
    public void setStat(int skill, int stat) {
        stats[skill] = stat;
    }

    /**
     *
     * @param skill
     * @return
     */
    public int getStat(int skill) {
        return stats[skill];
    }

    /**
     *
     * @param id
     * @param experience
     */
    public void setExperience(int id, double experience) {
        this.experience[id] = experience;
    }

    /**
     *
     * @param id
     * @return
     */
    public double getExperience(int id) {
        return experience[id];
    }

    /**
     *
     * @param skill
     * @param amount
     */
    public void giveExperience(int skill, double amount) {
        double curr = getExperience(skill);
        if(curr + amount > 200_000_000D) {
            amount = 200_000_000 - curr;
        }
        int prev = getLevel(skill);
        experience[skill] += amount;
        int currl = getLevel(skill);

        stats[skill] += currl - prev;
    }

    /**
     *
     * @param skill
     * @return
     */
    public int getLevel(int skill) {
        return getLevelFromExperience(experience[skill]);
    }

    /**
     *
     * @param xp
     * @return
     */
    private static int getLevelFromExperience(double xp) {
        int points = 0;
        int output = 0;

        for (int level = 1; level <= 99; level++) {
            points += Math.floor(level + 300.0 * Math.pow(2.0, level / 7.0));
            output = points / 4;

            if ((output - 1) >= xp)
                return level;
        }

        return 99;
    }
}
