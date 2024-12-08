package org.backend.cli.services;

import org.backend.cli.output.JsonWriter;
import org.backend.enums.EventTypes;
import org.backend.event.CustomerEvent;
import org.backend.event.EventPublisher;
import org.backend.model.Customer;
import org.backend.model.Purchase;
import org.backend.model.Ticket;
import org.backend.services.CustomerPool;
import org.backend.services.TicketPool;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerSimulation extends SimulationAbstract {

    private final Customer customer;
    private final CustomerPool customerPool;
    private final TicketPool ticketPool;
    private final int buyingInterval;
    private final int maxBuyingQuantity;
    private final EventPublisher eventPublisher = EventPublisher.getInstance();
    private int boughtQuantity;

    public CustomerSimulation(int buyingInterval, int maxBuyingQuantity, ThreadEventPasser threadEventPasser, Customer customer, CustomerPool customerPool, TicketPool ticketPool) {
        super(threadEventPasser);
        this.buyingInterval = buyingInterval;
        this.maxBuyingQuantity = maxBuyingQuantity;
        this.customer = customer;
        this.customerPool = customerPool;
        this.ticketPool = ticketPool;
        customerPool.addCustomer(customer);
        eventPublisher.publish(new CustomerEvent(customer, EventTypes.QUE, "Customer added successfully"));
    }

    private void simulateBuying() throws InterruptedException {
        for (Ticket ticket : ticketPool.getInUseTickets()) {
            if (boughtQuantity >= maxBuyingQuantity || threadEventPasser.receiveEvent().equals(EventTypes.REMOVED_THREAD)) {
                return;
            }
            String purchaseId = ticketPool.queTicket(ticket.getId(), customer);
            if (purchaseId != null) {
                eventPublisher.publish(new CustomerEvent(customer, EventTypes.QUE, "Ticket que successfully"));
                simulatePaying(purchaseId, customer);
            } else {
                eventPublisher.publish(new CustomerEvent(customer, EventTypes.QUE, "Ticket que failed"));
            }
            boughtQuantity++;
            Thread.sleep(buyingInterval);
        }
    }

    private void simulatePaying(String id, Customer customer) {
        boolean bought = ticketPool.buyTicket(id, customer);
        if (bought) {
            eventPublisher.publish(new CustomerEvent(customer, EventTypes.BUY, "Ticket buy successful"));
        } else {
            eventPublisher.publish(new CustomerEvent(customer, EventTypes.BUY, "Ticket buy failed"));
        }
    }

    @Override
    public void start() throws InterruptedException {
        simulateBuying();
    }

    @Override
    public void clearMem() {
        List<Purchase> removeCustomerPurchases = customerPool.removeCustomerPurchases(customer.getId());
        boolean removed = customerPool.removeCustomer(customer.getId());
        if (removed) {
            eventPublisher.publish(new CustomerEvent(customer, EventTypes.REMOVE, "Purchases removing successfully"));
        } else {
            eventPublisher.publish(new CustomerEvent(customer, EventTypes.REMOVE, "Purchases removing failed"));
        }
        JsonWriter.writeToJsonPretty(removeCustomerPurchases.stream().map(Purchase::toDto).collect(Collectors.toList()), "./customer-data/customer-" + customer.getId() + "-log.json");
    }

    @Override
    public void run() {
        try {
            start();
            clearMem();
            eventPublisher.publish(new CustomerEvent(customer, EventTypes.REMOVED_THREAD));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
