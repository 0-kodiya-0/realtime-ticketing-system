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
        this.id = this.customer.getId();
        this.ticketPool = ticketPool;
        this.purchasePool = purchasePool;
        if (customer.getType().equals(CustomerTypes.VIP)) {
            threadPriority = Thread.MAX_PRIORITY;
        } else {
            threadPriority = Thread.MIN_PRIORITY;
        }
    }

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

    @Override
    public void clearMem() {
        List<Purchase> removeCustomerPurchases = removeCustomerPurchases();
        try {
            JsonWriter.writeChunkedJsonFiles(FilePaths.CUSTOMER.toString(), "customer", id, removeCustomerPurchases.stream().map(Purchase::toDto).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
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
