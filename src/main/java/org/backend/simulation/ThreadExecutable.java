package org.backend.simulation;

public interface ThreadExecutable extends Runnable {
    void start() throws InterruptedException;

    void stop(boolean interruptThread);

    void clearMem();

    boolean isRunning();
}
