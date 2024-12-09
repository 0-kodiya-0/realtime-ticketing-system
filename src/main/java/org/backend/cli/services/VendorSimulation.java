package org.backend.cli.services;

import org.backend.cli.output.JsonWriter;
import org.backend.enums.EventTypes;
import org.backend.enums.TicketCategory;
import org.backend.event.EventMessage;
import org.backend.event.EventPublisher;
import org.backend.event.VendorEvent;
import org.backend.model.Ticket;
import org.backend.model.Vendor;
import org.backend.services.TicketPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class VendorSimulation extends SimulationAbstract {

    private final Random randomNumberFinder = new Random();

    private final TicketPool ticketPool;
    private final Vendor vendor;
    private final int sellingInterval;
    private final EventPublisher eventPublisher = EventPublisher.getInstance();
    private int ticketRemovalFileCreationCount = 0;

    public VendorSimulation(int sellingInterval, ThreadEventPasser threadEventPasser, Vendor vendor, TicketPool ticketPool) {
        super(threadEventPasser);
        this.sellingInterval = sellingInterval;
        this.vendor = vendor;
        this.ticketPool = ticketPool;
        eventPublisher.publish(new VendorEvent(vendor, EventTypes.ADD_THREAD, new EventMessage("Added successfully", Map.of("Vendor", vendor))));
    }

    private Ticket createTicket() {
        TicketCategory ticketCategory = TicketCategory.values()[randomNumberFinder.nextInt(TicketCategory.values().length)];
        return new Ticket(vendor, ticketCategory, randomNumberFinder.nextInt(2, 5));
    }

    public void simulateTicketSelling() throws InterruptedException {
        while (true) {
            if (threadEventPasser.receiveEvent().equals(EventTypes.REMOVED_THREAD)) {
                break;
            }
            if (ticketPool.isPoolFull()) {
                return;
            }
            Ticket ticket = createTicket();
            String isAdded = ticketPool.addTicket(ticket);
            if (isAdded != null) {
                eventPublisher.publish(new VendorEvent(vendor, EventTypes.ADD_TICKET, new EventMessage("Ticket adding successfully", Map.of("ticket", ticket))));
            } else {
                eventPublisher.publish(new VendorEvent(vendor, EventTypes.ADD_TICKET_FAILED, new EventMessage("Ticket adding failed", Map.of("ticket", ticket))));
            }
            Thread.sleep(sellingInterval);
        }
    }

    public void simulateTicketQuantityFullRemoval() {
        List<Ticket> removedTickets = new ArrayList<>();
        for (Ticket ticket : ticketPool.getInUseTickets()) {
            if (ticketPool.removeQuantityFullTickets(ticket.getId(), vendor)) {
                removedTickets.add(ticket);
            }
        }
        JsonWriter.writeToJsonPretty(removedTickets.stream().map(Ticket::toDto).collect(Collectors.toList()), "./vendor-data/vendor-" + vendor.getId() + "-" + ticketRemovalFileCreationCount + "-log.json");
        eventPublisher.publish(new VendorEvent(vendor, EventTypes.REMOVE_TICKET, new EventMessage("Ticket quantity full removal successfully", Map.of("Vendor", vendor, "removedTickets", removedTickets))));
        ticketRemovalFileCreationCount++;
    }

    @Override
    public void start() throws InterruptedException {
        while (!threadEventPasser.receiveEvent().equals(EventTypes.REMOVED_THREAD)) {
            simulateTicketSelling();
            Thread.sleep(5000);
            simulateTicketQuantityFullRemoval();
            Thread.sleep(5000);
        }
    }

    @Override
    public void stop() {
        clearMem();
        eventPublisher.publish(new VendorEvent(vendor, EventTypes.REMOVED_THREAD, new EventMessage("Removed successfully")));
    }


    @Override
    public void clearMem() {
        List<Ticket> removedTickets = new ArrayList<>();
        for (Ticket ticket : ticketPool.getInUseTickets()) {
            if (ticketPool.removeAllTickets(ticket.getId(), vendor)) {
                removedTickets.add(ticket);
            }
        }
        JsonWriter.writeToJsonPretty(removedTickets.stream().map(Ticket::toDto).collect(Collectors.toList()), "./vendor-data/vendor-" + vendor.getId() + "-" + ticketRemovalFileCreationCount + "-log.json");
        eventPublisher.publish(new VendorEvent(vendor, EventTypes.CLEAR_MEM, new EventMessage("Memory cleared successfully")));
        ticketRemovalFileCreationCount++;
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
