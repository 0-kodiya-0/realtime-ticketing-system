package org.backend.pools;

import lombok.Data;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

@Data
public abstract class PoolAbstract {
    protected final ConcurrentLinkedQueue<Object> inUseObjects = new ConcurrentLinkedQueue<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final int poolMaxCapacity;
    private int poolUsedCapacity = 0;

    public PoolAbstract() {
        poolMaxCapacity = 1000;
    }

    public PoolAbstract(int poolMaxCapacity) {
        this.poolMaxCapacity = poolMaxCapacity;
    }

    public void increasePoolUsedCapacity(Object object) {
        lock.lock();
        try {
            inUseObjects.add(object);
            poolUsedCapacity++;
        } finally {
            lock.unlock();
        }
    }

    public void decreasePoolUsedCapacity(Object object) {
        lock.lock();
        try {
            inUseObjects.remove(object);
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
