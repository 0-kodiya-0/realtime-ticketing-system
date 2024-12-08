package org.backend.cli.services;

import org.backend.cli.output.JsonWriter;
import org.backend.enums.EventTypes;
import org.backend.enums.TicketCategory;
import org.backend.event.EventPublisher;
import org.backend.event.VendorEvent;
import org.backend.model.Ticket;
import org.backend.model.Vendor;
import org.backend.services.TicketPool;
import org.backend.services.VendorPool;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class VendorSimulation extends SimulationAbstract {

    private final Random randomNumberFinder = new Random();

    private final VendorPool vendorPool;
    private final TicketPool ticketPool;
    private final Vendor vendor;
    private final int sellingInterval;
    private final int maxSellingQuantity;
    private final EventPublisher eventPublisher = EventPublisher.getInstance();

    public VendorSimulation(int sellingInterval, int maxSellingQuantity, ThreadEventPasser threadEventPasser, Vendor vendor, VendorPool vendorPool, TicketPool ticketPool) {
        super(threadEventPasser);
        this.sellingInterval = sellingInterval;
        this.maxSellingQuantity = maxSellingQuantity;
        this.vendor = vendor;
        this.vendorPool = vendorPool;
        this.ticketPool = ticketPool;
        vendorPool.addVendor(vendor);
        eventPublisher.publish(new VendorEvent(vendor, EventTypes.ADD_THREAD, "Vendor added successfully"));
    }

    private Ticket createTicket() {
        TicketCategory ticketCategory = TicketCategory.values()[randomNumberFinder.nextInt(TicketCategory.values().length)];
        return new Ticket(vendor, ticketCategory, randomNumberFinder.nextInt(2, 5));
    }

    @Override
    public void start() throws InterruptedException {
        for (int i = 0; i < maxSellingQuantity; i++) {
            if (threadEventPasser.receiveEvent().equals(EventTypes.REMOVED_THREAD)) {
                break;
            }
            Ticket ticket = createTicket();
            String isAdded = ticketPool.addTicket(ticket);
            if (isAdded != null) {
                eventPublisher.publish(new VendorEvent(vendor, EventTypes.ADD, "Ticket adding successfully"));
            } else {
                eventPublisher.publish(new VendorEvent(vendor, EventTypes.ADD, "Ticket adding failed"));
            }
            Thread.sleep(sellingInterval);
        }
    }

    @Override
    public void clearMem() {
        List<Ticket> removeVendorTickets = vendorPool.removeVendorTickets(vendor.getId());
        boolean removeVendor = vendorPool.removeVendor(vendor.getId());
        if (removeVendor){
            eventPublisher.publish(new VendorEvent(vendor, EventTypes.REMOVE, "Ticket removal successfully"));
        } else {
            eventPublisher.publish(new VendorEvent(vendor, EventTypes.REMOVE, "Ticket removal failed"));
        }
        JsonWriter.writeToJsonPretty(removeVendorTickets.stream().map(Ticket::toDto).collect(Collectors.toList()), "./vendor-data/vendor-" + vendor.getId() + "-log.json");
    }

    @Override
    public void run() {
        try {
            start();
            clearMem();
            eventPublisher.publish(new VendorEvent(vendor, EventTypes.REMOVED_THREAD));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
