package org.backend.pools;

import lombok.Getter;
import org.backend.thread.CustomThread;
import org.backend.thread.ThreadExecutableAbstract;

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
        return removedTasks;
    }

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
