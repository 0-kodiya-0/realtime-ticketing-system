package org.backend.services;

import org.backend.pools.PurchasePool;
import org.backend.pools.TicketPool;
import org.backend.simulation.ThreadExecutableAbstract;

import java.util.Date;

/**
 * LiveMonitoring is a subclass of `ThreadExecutableAbstract` that performs live monitoring.
 * It executes tasks continuously in a loop, typically designed to monitor certain conditions or metrics in a Command Line Interface (CLI) environment.
 * This class handles the logic for monitoring and reporting data as required by the application.
 */
public class LiveMonitoring extends ThreadExecutableAbstract {
    private final TicketPool ticketPool;
    private final PurchasePool purchasePool;

    public LiveMonitoring(TicketPool ticketPool, PurchasePool purchasePool) {
        this.ticketPool = ticketPool;
        this.purchasePool = purchasePool;
    }

    /**
     * Monitors the changes in the purchase and ticket pools.
     */
    private void periodEventMonitor() throws InterruptedException {
        Date liveMonitorStartTime = new Date();
        int purchasePoolBeforeCount = purchasePool.getInUseObjects().size();
        int ticketPoolBeforeCount = ticketPool.getInUseObjects().size();
        while (!stopHappeningOperations) {
            Date liveMonitorEndTime = new Date();
            System.out.println("------------------------------------------------------------");
            System.out.println("Live monitor status from (" + liveMonitorStartTime + ") to (" + liveMonitorEndTime + ")");
            System.out.println(" Purchase pool size : " + purchasePool.getInUseObjects().size());
            System.out.println(" Ticket pool size   : " + ticketPool.getInUseObjects().size());
            System.out.println("------------------------------------------------------------");

            // Calculate the number of newly added or removed purchases
            int newlyAddedPurchases = purchasePool.getInUseObjects().size() - purchasePoolBeforeCount;
            if (newlyAddedPurchases > 0) {
                System.out.println(" * " + newlyAddedPurchases + " new purchase history added");
            } else {
                System.out.println(" * " + -newlyAddedPurchases + " purchase history removed");
            }

            // Calculate the number of newly added or removed tickets
            int newlyAddedTickets = ticketPool.getInUseObjects().size() - ticketPoolBeforeCount;
            if (newlyAddedTickets > 0) {
                System.out.println(" * " + newlyAddedTickets + " new tickets added");
            } else {
                System.out.println(" * " + -newlyAddedTickets + " tickets removed");
            }

            purchasePoolBeforeCount = purchasePool.getInUseObjects().size();
            ticketPoolBeforeCount = ticketPool.getInUseObjects().size();
            liveMonitorStartTime = liveMonitorEndTime;
            Thread.sleep(2000);
        }
    }

    @Override
    public void run() {
        System.out.println("------------------------------------------------------------");
        System.out.println(" Live monitoring enabled");
        System.out.println("------------------------------------------------------------");
        try {
            start();
            stop(false);
        } catch (InterruptedException e) {
            stop(true);
        }
    }

    @Override
    public void start() throws InterruptedException {
        stopHappeningOperations = false;
        periodEventMonitor();
    }

    @Override
    public void stop(boolean interruptThread) {
        stopHappeningOperations = true;
        clearMem();
        if (interruptThread) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void clearMem() {
        return;
    }
}
