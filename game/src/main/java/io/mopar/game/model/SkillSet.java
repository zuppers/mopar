package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class SkillSet {

    private int[] stats = new int[Skills.COUNT];
    private double[] exp = new double[Skills.COUNT];

    public SkillSet() {
        for(int i = 0; i < stats.length; i++) {
            stats[i] = 1;
            exp[i] = 0.0D;
        }
        stats[Skills.HITPOINTS] = 10;
        exp[Skills.HITPOINTS] = 1184;
    }

    public void setStat(int skill, int stat) {
        stats[skill] = stat;
    }

    public int getStat(int skill) {
        return stats[skill];
    }

    public int getLevel(int skill) {
        return getLevelFromExperience(exp[skill]);
    }

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

    public double getExperience(int i) {
        return exp[i];
    }
}
