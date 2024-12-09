package org.backend.services;

public interface Simulation extends Runnable {
    void start() throws InterruptedException;

    void stop() throws InterruptedException;

    void clearMem();
}
