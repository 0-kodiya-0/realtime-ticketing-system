package org.backend.simulation;

import lombok.Data;

/**
 * ThreadExecutableAbstract is an abstract base class for implementing custom thread behavior.
 * It provides a base for handling thread lifecycle, including a mechanism to control whether thread operations should be stopped.
 * This class implements the `ThreadExecutable` interface, ensuring that all necessary methods are defined.
 */
@Data
public abstract class ThreadExecutableAbstract implements ThreadExecutable {
    /**
     * Uniquely identifiable id add from the thread
     */
    protected String id;
    /**
     * Manages the thread's operational state
     */
    protected boolean stopHappeningOperations = true;
    /**
     * The priority level of the thread. The default is set to `Thread.NORM_PRIORITY`
     */
    protected int threadPriority = Thread.NORM_PRIORITY;

    @Override
    public boolean isRunning() {
        return !stopHappeningOperations;
    }
}
