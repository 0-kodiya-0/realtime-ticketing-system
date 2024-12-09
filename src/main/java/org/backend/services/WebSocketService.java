package org.backend.services;

import org.backend.event.CustomerEvent;
import org.backend.event.EventListener;
import org.backend.event.EventPublisher;
import org.backend.event.VendorEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final EventPublisher eventPublisher = EventPublisher.getInstance();
    private final EventListener<CustomerEvent> customerEventListener;
    private final EventListener<VendorEvent> vendorEventListener;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.customerEventListener = new EventListener<CustomerEvent>() {
            @Override
            public void onEvent(CustomerEvent event) {
                messagingTemplate.convertAndSend("/topic/tickets", event);
            }
        };
        this.vendorEventListener = new EventListener<VendorEvent>() {
            @Override
            public void onEvent(VendorEvent event) {
                messagingTemplate.convertAndSend("/topic/tickets", event);
            }
        };
        eventPublisher.subscribe(CustomerEvent.class, this.customerEventListener);
        eventPublisher.subscribe(VendorEvent.class, this.vendorEventListener);
    }
}