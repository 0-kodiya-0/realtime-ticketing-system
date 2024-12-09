package org.backend.event;

import lombok.Data;
import org.backend.dto.DataToDto;
import org.backend.dto.Dto;

import java.util.Map;

@Data
public class EventMessage {
    private String message;
    private Map<String, DataToDto<? extends Dto>> eventData;

    public EventMessage(String message) {
        this.message = message;
    }

    public EventMessage(String message, Map<String, DataToDto<? extends Dto>> eventData) {
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
            for (Map.Entry<String, DataToDto<? extends Dto>> entry : eventData.entrySet()) {
                str.append(" ").append(entry.getKey()).append(": ").append(entry.getValue().toDto()).append("\n");
            }
        }
        return str.toString();
    }
}
