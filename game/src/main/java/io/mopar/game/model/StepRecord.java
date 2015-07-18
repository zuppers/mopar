package io.mopar.game.model;

/**
 * @author Hadyn Fitzgerald
 */
public class StepRecord {

    /**
     * The step.
     */
    private Step step;

    /**
     * The time.
     */
    private int time;

    /**
     * Constructs a new {@link StepRecord};
     *
     * @param step The step.
     * @param time The time the step was taken.
     */
    public StepRecord(Step step, int time) {
        this.step = step;
        this.time = time;
    }

    /**
     * Gets the step.
     *
     * @return The step.
     */
    public Step getStep() {
        return step;
    }

    /**
     * Gets the time the step was taken.
     *
     * @return The time.
     */
    public int getTime() {
        return time;
    }
}
