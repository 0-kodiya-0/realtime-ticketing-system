package org.backend.thread;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomThread extends Thread {
    private final ThreadExecutableAbstract target;

    public <T extends ThreadExecutableAbstract> CustomThread(T target) {
        super(target);
        this.target = target;
    }

    public boolean isTargetOfType(Class<?> clazz) {
        return clazz.isInstance(target);
    }
}