package org.backend.event;

import lombok.Data;

import java.util.Map;

@Data
public class EventMessage {
    private String message;
    private Map<String, Object> eventData;

    public EventMessage(String message) {
        this.message = message;
    }

    public EventMessage(String message, Map<String, Object> eventData) {
        this.message = message;
        this.eventData = eventData;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\n");
        str.append("Message : ").append(message).append(" ").append("\n");
        str.append("Event Data : ").append("\n");
        if (eventData == null) {
            str.append(" ").append("No event data available").append("\n");
        } else {
            for (Map.Entry<String, Object> entry : eventData.entrySet()) {
                str.append(" ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }
        return str.toString();
    }
}
