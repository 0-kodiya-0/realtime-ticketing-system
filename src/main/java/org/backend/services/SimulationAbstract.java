package org.backend.services;

public abstract class SimulationAbstract implements Simulation {
    protected final ThreadEventPasser threadEventPasser;

    protected SimulationAbstract(ThreadEventPasser threadEventPasser) {
        this.threadEventPasser = threadEventPasser;
    }
}
