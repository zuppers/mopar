package io.mopar.game.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Hadyn Fitzgerald
 */
public class Mobile extends Entity {

    /**
     * The maximum amount of recorded steps.
     */
    public static final int PREVIOUS_STEP_CAPACITY = 104;

    /**
     * The states.
     */
    private Queue<Integer> states = new ArrayDeque<>();

    /**
     * The waypoints.
     */
    private Queue<Waypoint> waypoints = new ArrayDeque<>();

    /**
     * The steps.
     */
    private Queue<Step> steps = new ArrayDeque<>();

    /**
     * The previous steps.
     */
    private Deque<Step> previousSteps = new ArrayDeque<>();

    /**
     * Flag for clipped movement.
     */
    private boolean clipped = true;

    /**
     * Flag for running.
     */
    private boolean running;

    /**
     * Flag for if the mobile walked.
     */
    private boolean moving;

    /**
     * Flag for teleporting.
     */
    private boolean teleporting;

    /**
     * The previous position.
     */
    private Position previousPosition;

    /**
     * Queues a state.
     *
     * @param state The state.
     */
    public void queueState(int state) {
        states.add(state);
    }

    /**
     * Gets the next state.
     *
     * @return The next state.
     */
    public int nextState() {
        return states.poll();
    }

    /**
     * Gets if mobile has pending states.
     *
     * @return If the state queue is not empty.
     */
    public boolean hasPendingState() {
        return !states.isEmpty();
    }

    /**
     *
     * @param x
     * @param y
     */
    public void addWaypoint(int x, int y) {
        addWaypoint(new Waypoint(x, y));
    }

    /**
     * Adds a waypoint for the mobile to walk to.
     *
     * @param point the point to walk to.
     */
    public void addWaypoint(Waypoint point) {
        waypoints.add(point);
    }

    /**
     *
     * @return
     */
    public Waypoint nextWaypoint() {
        return waypoints.poll();
    }

    /**
     *
     * @return
     */
    public boolean hasPendingWaypoints() {
        return !waypoints.isEmpty();
    }

    /**
     * Adds a step.
     *
     * @param step The step.
     */
    public void addStep(Step step) {
        steps.add(step);
    }

    /**
     * Adds steps.
     *
     * @param coll The steps.
     */
    public void addSteps(Collection<Step> coll) {
        steps.addAll(coll);
    }

    /**
     * Gets the next step.
     *
     * @return The next step or <code>null</code> if the step queue is empty.
     */
    public Step nextStep() {
        return steps.poll();
    }

    /**
     * Gets if the mobile has pending steps.
     *
     * @return If the step queue is not empty.
     */
    public boolean hasPendingSteps() {
        return !steps.isEmpty();
    }

    /**
     * Clears the step and waypoint queue.
     */
    public void clearRoute() {
        waypoints.clear();
        steps.clear();
    }

    /**
     * Records a step that was taken.
     *
     * @param step the step.
     */
    public void addPreviousStep(Step step) {
        if(previousSteps.size() == PREVIOUS_STEP_CAPACITY) {
            previousSteps.remove();
        }
        previousSteps.addLast(step);
    }

    /**
     * Gets the last traversed step.
     *
     * @return The last step or {@code null} if there are no recorded steps.
     */
    public Step getLastStep() {
        if(previousSteps.isEmpty()) {
            return null;
        }
        Step step = previousSteps.getLast();
        return step;
    }

    /**
     * Gets the recent steps.
     *
     * @return The recent steps.
     */
    public Queue<Step> getRecentSteps() {
        Deque<Step> steps = new ArrayDeque<>();

        Step previous = null;
        for(Iterator<Step> i = previousSteps.descendingIterator(); i.hasNext(); ) {
            Step record = i.next();
            if(previous != null && previous.getTime() != record.getTime()) {
                break;
            }

            if(previous == null || previous.getTime() == record.getTime()) {
                steps.addFirst(record);
                previous = record;
                continue;
            }
        }

        return steps;
    }

    /**
     * Gets the steps taken at a specific time.
     *
     * @param time The time.
     * @return The steps taken at the time.
     */
    public List<Step> getStepsAt(int time) {
        return previousSteps.stream().filter(step -> step.getTime() == time).collect(Collectors.toList());
    }


    /**
     * Sets if the mobile's movement is clipped.
     *
     * @param clipped The clipped flag.
     */
    public void setClipped(boolean clipped) {
        this.clipped = clipped;
    }

    /**
     * Gets if the mobile's movement is clipped.
     *
     * @return The clipped flag.
     */
    public boolean isClipped() {
        return clipped;
    }

    /**
     * Sets if the mobile is running.
     *
     * @param running The running flag.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Gets if the mobile is running.
     *
     * @return The running flag.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets if the mobile is moving.
     *
     * @param moving The moving flag.
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    /**
     * Gets if the mobile is moving.
     *
     * @return The moving flag.
     */
    public boolean isMoving() {
        return moving;
    }

    /**
     *
     * @param position
     */
    public void teleport(Position position) {
        setPosition(position);
        teleporting = true;
        clearRoute();
    }

    /**
     * Gets if the mobile is teleporting.
     *
     * @return The teleporting flag.
     */
    public boolean isTeleporting() {
        return teleporting;
    }

    /**
     *
     * @param previousPosition
     */
    public void setPreviousPosition(Position previousPosition) {
        this.previousPosition = previousPosition;
    }

    /**
     *
     * @return
     */
    public Position getPreviousPosition() {
        return previousPosition;
    }

    /**
     * Resets the mobile.
     */
    public void reset() {
        moving = false;
        teleporting = false;
    }

}
