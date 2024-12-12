package org.backend.simulation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.backend.enums.CustomerTypes;
import org.backend.enums.FilePaths;
import org.backend.io.file.JsonWriter;
import org.backend.model.Customer;
import org.backend.model.Purchase;
import org.backend.model.Ticket;
import org.backend.pools.PurchasePool;
import org.backend.pools.TicketPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a customer simulation that extends ThreadExecutableAbstract for concurrent execution.
 * Manages customer-specific behaviors and interactions with the ticket system.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerSimulation extends ThreadExecutableAbstract {

    private final Customer customer = new Customer();
    private final TicketPool ticketPool;
    private final PurchasePool purchasePool;
    private final int buyingInterval;

    public CustomerSimulation(int buyingInterval, CustomerTypes isCustomerType, TicketPool ticketPool, PurchasePool purchasePool) {
        this.buyingInterval = buyingInterval;
        this.customer.setType(isCustomerType);

        // Associated the thread id with the customer id
        this.id = this.customer.getId();
        this.ticketPool = ticketPool;
        this.purchasePool = purchasePool;

        // Sets the thread pool priority to max if the customer is vip
        if (customer.getType().equals(CustomerTypes.VIP)) {
            threadPriority = Thread.MAX_PRIORITY;
        } else {
            threadPriority = Thread.MIN_PRIORITY;
        }
    }

    /**
     * Simulates continuous ticket buying behavior for a customer at specified intervals.
     * Iterates through available tickets that haven't reached the full quantity.
     * Attempts to queue and purchase them.
     * Process includes queuing the ticket and completing the purchase with specified delays between operations.
     * @throws InterruptedException if the thread is interrupted during sleep intervals
     */
    private void simulateBuying() throws InterruptedException {
        while (!stopHappeningOperations) {
            for (Ticket ticket : ticketPool.findAllQuantityNotFullTicket()) {
                Purchase purchase = ticketPool.queTicket(ticket.getId(), customer);
                if (purchase != null) {
                    ticketPool.buyTicket(purchase, customer);
                }
                Thread.sleep(buyingInterval);
            }
            Thread.sleep(buyingInterval);
        }
    }

    /**
     * Processes and removes all purchases associated with the current customer from the pool.
     * Iterates through active purchases in the pool, attempts to remove each purchase
     * linked to the customer, and collects removed purchases in a list.
     * @return List<Purchase> collection of all successfully removed purchase records
     */
    private List<Purchase> removeCustomerPurchases() {
        List<Purchase> removedPurchases = new ArrayList<>();
        for (Object obj : purchasePool.getInUseObjects()) {
            Purchase purchase = (Purchase) obj;
            if (purchasePool.removePurchase(purchase, customer)) {
                removedPurchases.add(purchase);
            }
        }
        return removedPurchases;
    }

    @Override
    public void start() throws InterruptedException {
        if (!stopHappeningOperations) {
            throw new IllegalArgumentException("Thread already running");
        }
        stopHappeningOperations = false;
        simulateBuying();
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
     * Cleans up memory by removing customer purchases and saving them to JSON files.
     * Converts purchases to DTOs and writes them in chunks to maintain performance.
     * Files are saved with customer-specific identifiers in the specified path.
     * @throws IOException if there's an error writing the JSON files
     */
    @Override
    public void clearMem() {
        List<Purchase> removeCustomerPurchases = removeCustomerPurchases();
        try {
            JsonWriter.writeChunkedJsonFiles(FilePaths.CUSTOMER.toString(), "customer", id, removeCustomerPurchases.stream().map(Purchase::toDto).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            start();
        } catch (InterruptedException e) {
            stop(true);
        } catch (Exception e) {
            stop(true);
            throw new RuntimeException(e);
        }
    }
}
