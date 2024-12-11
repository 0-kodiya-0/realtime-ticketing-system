package org.backend.simulation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.backend.enums.FilePaths;
import org.backend.io.file.JsonWriter;
import org.backend.model.Ticket;
import org.backend.model.Vendor;
import org.backend.pools.TicketPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class VendorSimulation extends ThreadExecutableAbstract {

    private final Random randomNumberFinder = new Random();

    private final TicketPool ticketPool;
    private final Vendor vendor = new Vendor();
    private final int sellingInterval;
    private int ticketRemovalFileCreationCount = 0;

    public VendorSimulation(int sellingInterval, TicketPool ticketPool) {
        this.sellingInterval = sellingInterval;
        this.id = this.vendor.getId();
        this.ticketPool = ticketPool;
    }

    public void simulateTicketSelling() throws InterruptedException {
        while (!stopHappeningOperations) {
            if (ticketPool.isPoolFull()) {
                return;
            }
            Ticket ticket = new Ticket(vendor, randomNumberFinder.nextInt(2, 5));
            ticketPool.addTicket(ticket);
            Thread.sleep(sellingInterval);
        }
    }

    public void simulateTicketQuantityFullRemoval() {
        List<Ticket> removedTickets = new ArrayList<>();
        for (Object object : ticketPool.getInUseObjects()) {
            Ticket ticket = (Ticket) object;
            if (ticketPool.removeQuantityFullTickets(ticket, vendor)) {
                removedTickets.add(ticket);
            }
        }
        if (!removedTickets.isEmpty()) {
            JsonWriter.writeToJsonPretty(removedTickets.stream().map(Ticket::toDto).collect(Collectors.toList()), FilePaths.VENDOR + "/vendor-" + vendor.getId() + "-" + ticketRemovalFileCreationCount + "-log.json");
        }
        ticketRemovalFileCreationCount++;
    }

    @Override
    public void start() throws InterruptedException {
        if (!stopHappeningOperations) {
            throw new IllegalArgumentException("Thread already running");
        }
        stopHappeningOperations = false;
        while (!stopHappeningOperations) {
            simulateTicketSelling();
            Thread.sleep(5000);
            simulateTicketQuantityFullRemoval();
            Thread.sleep(5000);
        }
    }

    @Override
    public void stop(boolean interruptThread) {
        if (stopHappeningOperations) {
            throw new IllegalArgumentException("Thread already stopped");
        }
        stopHappeningOperations = true;
        clearMem();
        if (interruptThread) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void clearMem() {
        List<Ticket> removedTickets = new ArrayList<>();
        for (Object obj : ticketPool.getInUseObjects()) {
            Ticket ticket = (Ticket) obj;
            if (ticketPool.removeAllTickets(ticket, vendor)) {
                removedTickets.add(ticket);
            }
        }
        try {
            JsonWriter.writeChunkedJsonFiles(FilePaths.VENDOR.toString(), "vendor", id, removedTickets.stream().map(Ticket::toDto).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ticketRemovalFileCreationCount++;
    }

    @Override
    public void run() {
        try {
            start();
        } catch (InterruptedException _) {
            stop(true);
        } catch (Exception e) {
            stop(true);
            throw new RuntimeException(e);
        }
    }
}
