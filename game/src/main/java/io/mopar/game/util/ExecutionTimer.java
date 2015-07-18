package io.mopar.game.util;

/**
 * @author Hadyn Fitzgerald
 *
 * TODO(sinisoul): Come up with a better name for this, yes I know its ripped. Smh if you care.
 */
public class ExecutionTimer {

    /**
     * The next time to sleep.
     */
    private long next;

    /**
     * Sleeps the current executing thread.
     *
     * @param minimum The minimum amount of time to sleep in milliseconds.
     * @param delta The amount of time to sleep between executions in milliseconds.
     * @return The amount of elapsed executions from the last slept time up to at most 4 and <b>always</b> at least 1.
     */
    public int sleep(long minimum,  long delta) {
        long min = 1000000L * (long) minimum;
        long diff = next - System.nanoTime();
        if (diff < min) {
            diff = min;
        }

        try {
            long time = diff / 1000000L;
            if(time > 0) {
                Thread.sleep(time);
            }
        } catch (InterruptedException ex) {

        }

        long curr = System.nanoTime();
        int count;
        for (count = 0; count < 5 && (count < 1 || curr > next); next += 1000000L * delta) {
            count++;
        }
        if (next < curr) {
            next = curr;
        }
        return count;
    }

    /**
     * Resets the next sleep time.
     */
    public void reset() {
        next = System.nanoTime();
    }
}
