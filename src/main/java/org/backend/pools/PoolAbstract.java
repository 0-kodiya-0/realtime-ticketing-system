package org.backend.pools;

import lombok.Getter;

import java.util.concurrent.locks.ReentrantLock;

@Getter
public abstract class PoolAbstract {
    private final ReentrantLock lock = new ReentrantLock();
    private final int poolMaxCapacity;
    private int poolUsedCapacity = 0;

    public PoolAbstract(int poolMaxCapacity) {
        this.poolMaxCapacity = poolMaxCapacity;
    }

    public void increasePoolUsedCapacity() {
        lock.lock();
        try {
            poolUsedCapacity++;
        } finally {
            lock.unlock();
        }
    }

    public void decreasePoolUsedCapacity() {
        lock.lock();
        try {
            poolUsedCapacity--;
        } finally {
            lock.unlock();
        }
    }

    public boolean isPoolFull() {
        lock.lock();
        try {
            return poolUsedCapacity >= poolMaxCapacity;
        } finally {
            lock.unlock();
        }
    }

    public boolean isPoolEmpty() {
        lock.lock();
        try {
            return poolUsedCapacity == 0;
        } finally {
            lock.unlock();
        }
    }
}
