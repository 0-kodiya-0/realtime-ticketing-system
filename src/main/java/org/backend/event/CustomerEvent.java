package org.backend.event;

import lombok.Getter;
import org.backend.enums.EventTypes;
import org.backend.model.Customer;

@Getter
public class CustomerEvent extends Event {
    private final Customer customer;
    private final EventTypes eventType;
    private String message;

    public CustomerEvent(Customer customer, EventTypes eventType) {
        this.customer = customer;
        this.eventType = eventType;
    }

    public CustomerEvent(Customer customer, EventTypes eventType, String message) {
        this.customer = customer;
        this.eventType = eventType;
        this.message = message;
    }
}
