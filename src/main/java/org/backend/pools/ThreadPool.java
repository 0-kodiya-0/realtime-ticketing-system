package org.backend.pools;

import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPool extends PoolAbstract{

    private final ThreadPoolExecutor threadPool;

    public ThreadPool(int poolMaxCapacity, ThreadPoolExecutor threadPool) {
        super(poolMaxCapacity);
        this.threadPool = threadPool;
    }
}
