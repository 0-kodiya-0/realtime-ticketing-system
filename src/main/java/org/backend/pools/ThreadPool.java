package org.backend.pools;

import lombok.Getter;
import org.backend.services.Simulation;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ThreadPool extends PoolAbstract {

    public ThreadPool(int poolMaxCapacity) {
        super(poolMaxCapacity);
    }

    public <T extends Simulation> Thread addAndExecute(T task, int threadPriority) {
        if (isPoolFull()) {
            return null;
        }
        Thread thread = new Thread(task);
        increasePoolUsedCapacity(thread);
        thread.start();
        return thread;
    }

    public <T extends Simulation> List<Thread> add(T task, int repetitions, int threadPriority) {
        List<Thread> addedTasks = new ArrayList<>();
        boolean isPoolFull = false;
        if ((getPoolMaxCapacity() - getPoolUsedCapacity()) < repetitions) {
            return addedTasks;
        }
        for (int i = 0; i < repetitions; i++) {
            if (isPoolFull()) {
                isPoolFull = true;
                break;
            }
            Thread thread = new Thread(task);
            thread.setPriority(threadPriority);
            increasePoolUsedCapacity(thread);
            addedTasks.add(thread);
        }
        if (isPoolFull) {
            for (Thread addedTask : addedTasks) {
                shutdown(addedTask);
                addedTasks.remove(addedTask);
            }
        }
        return addedTasks;
    }

    public List<Thread> executeAll() {
        List<Thread> executingTasks = new ArrayList<>();
        for (Object obj : inUseObjects) {
            Thread thread = (Thread) obj;
            while (!thread.isAlive()) {
                thread.start();
            }
            executingTasks.add(thread);
        }
        return executingTasks;
    }

    public Thread shutdown(Thread task) {
        for (Object obj : inUseObjects) {
            Thread thread = (Thread) obj;
            if (thread.equals(task)) {
                inUseObjects.remove(obj);
                while (thread.isAlive()) {
                    task.interrupt();
                }
                return thread;
            }
        }
        return null;
    }

//    public List<Thread> interruptAll() {
//        List<Thread> interruptedTasks = new ArrayList<>();
//        for (Object obj : inUseObjects) {
//            Thread thread = (Thread) obj;
//            interruptedTasks.add(thread);
//            while (thread.isAlive()) {
//                thread.interrupt();
//            }
//        }
//        return interruptedTasks;
//    }

    public List<Thread> shutdownAll() {
        List<Thread> removedTasks = new ArrayList<>();
        for (Object obj : inUseObjects) {
            Thread thread = (Thread) obj;
            inUseObjects.remove(obj);
            removedTasks.add(thread);
            while (thread.isAlive()) {
                thread.interrupt();
            }
        }
        return removedTasks;
    }
}
