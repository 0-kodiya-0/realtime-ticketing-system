package org.backend.event;

import lombok.Getter;

import java.util.Date;

@Getter
public abstract class Event {
    private final Date timestamp;
    private EventMessage eventMessage;

    public Event() {
        this.timestamp = new Date();
    }

    public Event(EventMessage eventMessage) {
        this.timestamp = new Date();
        this.eventMessage = eventMessage;
    }

    @Override
    public String toString() {
        return "Event Timestamp : " + timestamp + "\n" +
                eventMessage;
    }
}
