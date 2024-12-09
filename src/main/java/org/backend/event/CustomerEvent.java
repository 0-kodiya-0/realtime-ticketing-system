package org.backend.event;

import lombok.Getter;
import org.backend.enums.EventTypes;
import org.backend.model.Customer;

@Getter
public class CustomerEvent extends Event {
    private final Customer customer;
    private final EventTypes eventType;

    public CustomerEvent(Customer customer, EventTypes eventType) {
        super();
        this.customer = customer;
        this.eventType = eventType;
    }

    public CustomerEvent(Customer customer, EventTypes eventType, EventMessage eventMessage) {
        super(eventMessage);
        this.customer = customer;
        this.eventType = eventType;
    }
}
