package org.backend.thread;

import lombok.Data;

@Data
public abstract class ThreadExecutableAbstract implements ThreadExecutable {
    protected String id;
    protected boolean stopHappeningOperations = true;
    protected int threadPriority = Thread.NORM_PRIORITY;

    @Override
    public boolean isRunning() {
        return !stopHappeningOperations;
    }
}
