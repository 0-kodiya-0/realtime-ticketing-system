package org.backend.services;

import org.backend.enums.EventTypes;
import org.backend.event.*;
import org.backend.model.Customer;
import org.backend.model.Purchase;
import org.backend.model.Ticket;
import org.backend.output.JsonWriter;
import org.backend.pools.PurchasePool;
import org.backend.pools.TicketPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerSimulation extends SimulationAbstract {

    private final Customer customer;
    private final TicketPool ticketPool;
    private final PurchasePool purchasePool;
    private final int buyingInterval;
    private final EventPublisher eventPublisher = EventPublisher.getInstance();
    private final List<Ticket> newlyAddedTickets = new ArrayList<>();
    private EventListener<VendorEvent> vendorEventEventListener;

    public CustomerSimulation(int buyingInterval, ThreadEventPasser threadEventPasser, Customer customer, TicketPool ticketPool, PurchasePool purchasePool) {
        super(threadEventPasser);
        this.buyingInterval = buyingInterval;
        this.customer = customer;
        this.ticketPool = ticketPool;
        this.purchasePool = purchasePool;
        eventPublisher.publish(new CustomerEvent(customer, EventTypes.QUE_TICKET, new EventMessage("Thread added successfully")));
    }

    private EventListener<VendorEvent> configureEvent() {
        return new EventListener<VendorEvent>() {
            @Override
            public void onEvent(VendorEvent event) {
                if (event.getEventType().equals(EventTypes.ADD_TICKET) && event.getEventMessage() != null) {
                    Ticket ticket = (Ticket) event.getEventMessage().getEventData().get("ticket");
                    if (ticket != null) {
                        synchronized (newlyAddedTickets) {
                            newlyAddedTickets.add(ticket);
                            newlyAddedTickets.notifyAll();
                        }
                    }
                }
            }
        };
    }

    private void simulatePaying(String id, Customer customer) {
        Purchase purchase = ticketPool.buyTicket(id, customer);
        if (purchase != null) {
            eventPublisher.publish(new CustomerEvent(customer, EventTypes.BUY_TICKET, new EventMessage("Ticket buy successful", Map.of("purchase", purchase))));
        } else {
            eventPublisher.publish(new CustomerEvent(customer, EventTypes.BUY_TICKET_FAILED, new EventMessage("Ticket buy failed")));
        }
    }

    private void simulateBuying() throws InterruptedException {
        while (!(threadEventPasser.receiveEvent().equals(EventTypes.REMOVED_THREAD))) {
            synchronized (newlyAddedTickets) {
                if (newlyAddedTickets.isEmpty()) {
                    newlyAddedTickets.wait();
                    continue;
                }
            }
            for (Ticket ticket : new ArrayList<>(newlyAddedTickets)) {
                Purchase purchase = ticketPool.queTicket(ticket.getId(), customer);
                if (purchase != null) {
                    eventPublisher.publish(new CustomerEvent(customer, EventTypes.QUE_TICKET, new EventMessage("Ticket que successfully", Map.of("ticket", ticket, "que", purchase))));
                    simulatePaying(purchase.getId(), customer);
                } else {
                    eventPublisher.publish(new CustomerEvent(customer, EventTypes.QUE_TICKET_FAILED, new EventMessage("Ticket que failed", Map.of("ticket", ticket))));
                }
                synchronized (newlyAddedTickets) {
                    newlyAddedTickets.remove(ticket);
                }
                Thread.sleep(buyingInterval);
            }
        }
    }

    private List<Purchase> removeCustomerPurchases() {
        List<Purchase> removedPurchases = new ArrayList<>();
        for (Object obj : purchasePool.getInUseObjects()) {
            Purchase purchase = (Purchase) obj;
            if (purchasePool.removePurchase(purchase.getId(), customer)) {
                removedPurchases.add(purchase);
            } else {
                eventPublisher.publish(new CustomerEvent(customer, EventTypes.REMOVE_PURCHASE_FAILED, new EventMessage("Purchases removing failed", Map.of("purchase", purchase))));
            }
        }
        eventPublisher.publish(new CustomerEvent(customer, EventTypes.REMOVE_PURCHASE, new EventMessage("Purchases removing successfully")));
        return removedPurchases;
    }

    @Override
    public void start() throws InterruptedException {
        vendorEventEventListener = configureEvent();
        eventPublisher.subscribe(VendorEvent.class, vendorEventEventListener);
        simulateBuying();
    }

    @Override
    public void stop() {
        clearMem();
        eventPublisher.publish(new CustomerEvent(customer, EventTypes.REMOVED_THREAD, new EventMessage("Thread removed successfully")));
    }

    @Override
    public void clearMem() {
        eventPublisher.unsubscribe(VendorEvent.class, vendorEventEventListener);
        List<Purchase> removeCustomerPurchases = removeCustomerPurchases();
        JsonWriter.writeToJsonPretty(removeCustomerPurchases.stream().map(Purchase::toDto).collect(Collectors.toList()), "./customer-data/customer-" + customer.getId() + "-log.json");
        eventPublisher.publish(new CustomerEvent(customer, EventTypes.CLEAR_MEM, new EventMessage("Memory cleared successfully")));
    }

    @Override
    public void run() {
        try {
            start();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
