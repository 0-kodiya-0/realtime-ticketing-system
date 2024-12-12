package org.backend.simulation;

/**
 * ThreadExecutable is an interface for defining custom thread behavior.
 * It extends the standard Runnable interface and adds methods to control thread execution,
 * manage memory, and check the running status of the thread.
 */
public interface ThreadExecutable extends Runnable {
    /**
     * Starts the thread execution.
     *
     * @throws InterruptedException If the thread is interrupted while starting
     */
    void start() throws InterruptedException;

    /**
     * Stops the thread execution.
     *
     * @param interruptThread If true, attempts to interrupt the thread; if false, stops the thread gracefully
     */
    void stop(boolean interruptThread);

    /**
     * Clears any allocated memory or resources associated with the thread.
     */
    void clearMem();

    /**
     * Checks if the thread is currently running.
     *
     * @return True if the thread is running, false otherwise
     */
    boolean isRunning();
}
