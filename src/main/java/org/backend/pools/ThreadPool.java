package org.backend.pools;

import lombok.Getter;
import org.backend.simulation.CustomThread;
import org.backend.simulation.ThreadExecutableAbstract;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Getter
public class ThreadPool extends PoolAbstract {

    private final List<CustomThread> activeThreads = new ArrayList<>();

    public ThreadPool(int poolMaxCapacity) {
        super(poolMaxCapacity);
    }

    public <T extends ThreadExecutableAbstract> List<T> findTargetClassThread(Class<T> target) {
        List<T> foundThreads = new ArrayList<>();
        for (Object obj : inUseObjects) {
            ThreadExecutableAbstract thread = (ThreadExecutableAbstract) obj;
            if (target.isInstance(thread)) {
                foundThreads.add((T) thread);
            }
        }
        return foundThreads;
    }

    public List<ThreadExecutableAbstract> findInterruptedThreads() {
        List<ThreadExecutableAbstract> foundThreads = new ArrayList<>();
        for (CustomThread thread : activeThreads) {
            if (thread.isInterrupted()) {
                foundThreads.add(thread.getTarget());
            }
        }
        return foundThreads;
    }

    public List<CustomThread> findRunningThreads() {
        return activeThreads;
    }

    public <T extends ThreadExecutableAbstract> int addRunnable(Supplier<T> task, int repetitions) {
        int addedTasks = 0;
        if ((getPoolMaxCapacity() - getPoolUsedCapacity()) < repetitions) {
            throw new IllegalArgumentException("Repetitions exceeded the pool max capacity");
        }
        for (int i = 0; i < repetitions; i++) {
            if (isPoolFull()) {
                break;
            }
            increasePoolUsedCapacity(task.get());
            addedTasks++;
        }
        return addedTasks;
    }

    /**
     * Removes specified number of running tasks of a given type from the thread pool.
     * Checks pool state and removes tasks that match the target class type up to the specified repetitions count.
     * @param targetClass Class type of threads to be removed
     * @param repetitions Number of thread tasks to remove
     * @return int Number of successfully removed tasks
     * @throws IllegalArgumentException if the pool is empty
     */
    public <T extends ThreadExecutableAbstract> int removeRunnable(Class<T> targetClass, int repetitions) {
        int removedTasks = 0;
        if (getInUseObjects().isEmpty()) {
            throw new IllegalArgumentException("Pool is empty");
        }
        if (getPoolUsedCapacity() < repetitions) {
            throw new IllegalArgumentException("Repetitions exceeded the pool max capacity");
        }
        int currentRepetitions = 0;
        for (Object obj : inUseObjects) {
            if (repetitions == currentRepetitions) {
                break;
            }
            if (obj == null) {
                continue;
            }
            ThreadExecutableAbstract runnable = (ThreadExecutableAbstract) obj;
            if (!targetClass.isInstance(runnable)) {
                continue;
            }
            decreasePoolUsedCapacity(obj);
            removedTasks++;
            currentRepetitions++;
        }
        // error time
        return removedTasks;
    }

    /**
     * Initiates execution of all non-running threads of a specific type in the pool.
     * Iterates through in-use objects, identifies threads of target class type that aren't running, and starts their execution.
     * @param targetClass Class type of threads to be started
     * @return int Number of threads successfully started
     */
    public int startNotRunningThreads(Class<? extends ThreadExecutableAbstract> targetClass) {
        int startedThreads = 0;
        for (Object obj : inUseObjects) {
            if (!targetClass.isInstance(obj)) {
                continue;
            }
            boolean threadActive = false;
            for (CustomThread thread : activeThreads) {
                if (targetClass.cast(obj).equals(thread.getTarget())) {
                    threadActive = true;
                    break;
                }
            }
            if (!threadActive) {
                CustomThread thread = new CustomThread(targetClass.cast(obj));
                activeThreads.add(thread);
                // Sets thread priority
                thread.setPriority(targetClass.cast(obj).getThreadPriority());
                thread.start();
                startedThreads++;
            }
        }
        return startedThreads;
    }

    public int stopRunningThreads(Class<? extends ThreadExecutableAbstract> targetClass) {
        int removedThreads = 0;
        for (CustomThread thread : new ArrayList<>(activeThreads)) {
            if (thread == null || thread.getTarget() == null || !targetClass.isInstance(thread.getTarget()) || !thread.getTarget().isRunning()) {
                continue;
            }
            thread.interrupt();
            activeThreads.remove(thread);
            removedThreads++;
        }
        return removedThreads;
    }
}
