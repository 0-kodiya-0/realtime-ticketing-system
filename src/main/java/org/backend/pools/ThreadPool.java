package org.backend.pools;

import lombok.Getter;
import org.backend.services.CustomerSimulation;
import org.backend.services.Simulation;
import org.backend.services.VendorSimulation;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ThreadPool extends PoolAbstract {

    public ThreadPool(int poolMaxCapacity) {
        super(poolMaxCapacity);
    }

    public <T extends Simulation> Thread execute(T task) {
        if (isPoolFull()) {
            return null;
        }
        Thread thread = new Thread(task);
        increasePoolUsedCapacity(thread);
        thread.start();
        return thread;
    }

    public <T extends Simulation> List<T> execute(T task, int repetitions) {
        List<T> addedTasks = new ArrayList<>();
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
            increasePoolUsedCapacity(thread);
            addedTasks.add(task);
            thread.start();
        }
        if (isPoolFull) {
            for (T addedTask : addedTasks) {
                shutdown(addedTask);
            }
        }
        return addedTasks;
    }

    public <T extends Simulation> void shutdown(T task) {
        for (Object obj : inUseObjects) {
            if (obj.equals(task)) {
                inUseObjects.remove(obj);
                try {
                    task.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void shutdownAllCustomers() {
        for (Object obj : inUseObjects) {
            if (obj instanceof CustomerSimulation customer) {
                customer.stop();
            }
        }
    }

    public void shutDownAllVendors() {
        for (Object obj : inUseObjects) {
            if (obj instanceof VendorSimulation vendor) {
                vendor.stop();
            }
        }
    }

    public void shutdownAll() {
        for (Object obj : inUseObjects) {
            Simulation sim = (Simulation) obj;
            try {
                sim.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
