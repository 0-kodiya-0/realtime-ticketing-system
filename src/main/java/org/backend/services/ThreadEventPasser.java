package org.backend.services;

import org.backend.enums.EventTypes;

public class ThreadEventPasser {
    private EventTypes eventType = EventTypes.START_THREAD;

    public synchronized void sendEvent(EventTypes eventType) {
        this.eventType = eventType;
    }

    public EventTypes receiveEvent() {
        return eventType;
    }
}