package org.backend.cli.services;

import org.backend.enums.EventTypes;

public class ThreadEventPasser {
    private EventTypes eventType = EventTypes.CONTINUE_THREAD;

    public synchronized void sendEvent(EventTypes eventType) {
        this.eventType = eventType;
    }

    public EventTypes receiveEvent() {
        return eventType;
    }
}