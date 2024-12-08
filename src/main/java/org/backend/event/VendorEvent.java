package org.backend.event;

import lombok.Getter;
import org.backend.enums.EventTypes;
import org.backend.model.Vendor;

@Getter
public class VendorEvent extends Event {
    private final Vendor vendor;
    private final EventTypes eventType;
    private String message;

    public VendorEvent(Vendor vendor, EventTypes eventType) {
        this.vendor = vendor;
        this.eventType = eventType;
    }

    public VendorEvent(Vendor vendor, EventTypes eventType, String message) {
        this.vendor = vendor;
        this.eventType = eventType;
        this.message = message;
    }
}
