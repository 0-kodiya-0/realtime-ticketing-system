package org.backend.event;

import lombok.Getter;
import org.backend.enums.EventTypes;
import org.backend.model.Ticket;
import org.backend.model.Vendor;

@Getter
public class VendorEvent extends Event {
    private final Vendor vendor;
    private final EventTypes eventType;

    public VendorEvent(Vendor vendor, EventTypes eventType) {
        super();
        this.vendor = vendor;
        this.eventType = eventType;
    }

    public VendorEvent(Vendor vendor, EventTypes eventType, EventMessage eventMessage) {
        super(eventMessage);
        this.vendor = vendor;
        this.eventType = eventType;
    }
}
