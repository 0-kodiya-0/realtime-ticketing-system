package org.backend.simulation;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CustomThread is an extension of the standard Java Thread class that integrates a target object of type `ThreadExecutableAbstract`.
 * It provides functionality to check if the target object matches a given class type. (helps in the ThreadPoolClass)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomThread extends Thread {
    private final ThreadExecutableAbstract target;

    /**
     * Constructs a new CustomThread with the specified target.
     *
     * @param <T> Type of the target object, which must be a subclass of `ThreadExecutableAbstract`
     * @param target Target object to be executed by the thread
     */
    public <T extends ThreadExecutableAbstract> CustomThread(T target) {
        super(target);
        this.target = target;
    }

    /**
     * Checks if the target object is of the specified class type.
     *
     * @param clazz Class type to check against
     * @return Returns true if the target is an instance of the specified class, false otherwise
     */
    public boolean isTargetOfType(Class<?> clazz) {
        return clazz.isInstance(target);
    }
}