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

/**
 * Represents a vendor simulation that extends ThreadExecutableAbstract for concurrent execution.
 * Manages vendor-specific ticket operations and simulations.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VendorSimulation extends ThreadExecutableAbstract {

    /**
     * To generate random values when creating the ticket
     */
    private final Random randomNumberFinder = new Random();

    private final TicketPool ticketPool;
    private final Vendor vendor = new Vendor();
    private final int sellingInterval;
    private int ticketRemovalFileCreationCount = 0;

    public VendorSimulation(int sellingInterval, TicketPool ticketPool) {
        this.sellingInterval = sellingInterval;

        // Associated the thread id with the vendor id
        this.id = this.vendor.getId();
        this.ticketPool = ticketPool;
    }

    /**
     * Continuously simulates ticket selling operations by adding new tickets to the pool.
     * Creates tickets with random quantities (2-4) at specified intervals until stopped
     * or pool is full. The selling process runs on a separate thread with configurable
     * intervals between sales.
     * @throws InterruptedException if the thread is interrupted during sleep
     */
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

    /**
     * Simulates the complete removal of tickets from the ticket pool for a vendor.
     * Processes each ticket in the pool, attempts to remove those with full quantity,
     * and logs the removed tickets to a JSON file with an incremental counter.
     * The log file is only created if at least one ticket was removed.
     */
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

            // Ticket pool item freeing
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

    /**
     * Cleans up memory by removing all tickets from the pool and saving them to JSON files.
     * Processes each ticket in the pool, attempts complete removal, and logs removed tickets to chunked JSON files
     * for efficient storage. Files are saved with vendor-specific identifiers and incremental counters.
     * @throws IOException if there's an error writing the JSON files
     */
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

            // Writes the removed tickets to .json file to clear memory
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
