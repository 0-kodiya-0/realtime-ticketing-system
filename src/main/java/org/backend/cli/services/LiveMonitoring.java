package org.backend.cli.services;

import org.backend.cli.enums.LiveMonitorType;
import org.backend.enums.EventTypes;
import org.backend.event.CustomerEvent;
import org.backend.event.EventListener;
import org.backend.event.EventPublisher;
import org.backend.event.VendorEvent;
import org.backend.services.PurchasePool;
import org.backend.services.TicketPool;

import java.util.Date;

public class LiveMonitoring implements Simulation {
    private final TicketPool ticketPool;
    private final PurchasePool purchasePool;
    private final EventPublisher publisher = EventPublisher.getInstance();
    private final LiveMonitorType liveMonitoringType;
    private final ThreadEventPasser threadEventPasser;

    private EventListener<CustomerEvent> customerEventListener;
    private EventListener<VendorEvent> vendorEventListener;

    public LiveMonitoring(LiveMonitorType liveMonitoringType, ThreadEventPasser threadEventPasser, TicketPool ticketPool, PurchasePool purchasePool) {
        this.liveMonitoringType = liveMonitoringType;
        this.threadEventPasser = threadEventPasser;
        this.ticketPool = ticketPool;
        this.purchasePool = purchasePool;
    }

    public void individualEventMonitor() {
        this.customerEventListener = new EventListener<CustomerEvent>() {
            @Override
            public void onEvent(CustomerEvent event) {
                System.out.println(event);
            }
        };
        this.vendorEventListener = new EventListener<VendorEvent>() {
            @Override
            public void onEvent(VendorEvent event) {
                System.out.println(event);
            }
        };
        publisher.subscribe(CustomerEvent.class, this.customerEventListener);
        publisher.subscribe(VendorEvent.class, this.vendorEventListener);
    }

    private void periodEventMonitor() {
        Date liveMonitorStartTime = new Date();
        int purchasePoolBeforeCount = purchasePool.getInUsePurchaseHistory().size();
        int ticketPoolBeforeCount = ticketPool.getInUseTickets().size();
        while (!threadEventPasser.receiveEvent().equals(EventTypes.REMOVED_THREAD)) {
            try {
                Date liveMonitorEndTime = new Date();
                System.out.println("------------------------------------------------------------");
                System.out.println("Live monitor status from (" + liveMonitorStartTime + ") to (" + liveMonitorEndTime + ")");
                System.out.println(" Purchase pool size : " + purchasePool.getInUsePurchaseHistory().size());
                System.out.println(" Ticket pool size   : " + ticketPool.getInUseTickets().size());
                System.out.println("---------------------");
                int newlyAddedPurchases = purchasePool.getInUsePurchaseHistory().size() - purchasePoolBeforeCount;
                if (newlyAddedPurchases > 0) {
                    System.out.println(" * " + newlyAddedPurchases + " new purchase history added");
                } else {
                    System.out.println(" * " + -newlyAddedPurchases + " purchase history removed");
                }
                int newlyAddedTickets = ticketPool.getInUseTickets().size() - ticketPoolBeforeCount;
                if (newlyAddedTickets > 0) {
                    System.out.println(" * " + newlyAddedTickets + " new tickets added");
                } else {
                    System.out.println(" * " + -newlyAddedTickets + " tickets removed");
                }
                purchasePoolBeforeCount = purchasePool.getInUsePurchaseHistory().size();
                ticketPoolBeforeCount = ticketPool.getInUseTickets().size();
                liveMonitorStartTime = liveMonitorEndTime;
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("------------------------------------------------------------");
                System.out.println(" Live monitoring thread interrupted");
                System.out.println("------------------------------------------------------------");
            }
        }
    }

    @Override
    public void run() {
        System.out.println("------------------------------------------------------------");
        System.out.println(" Live monitoring enabled");
        System.out.println("------------------------------------------------------------");
        start();
    }

    @Override
    public void start() {
        if (liveMonitoringType.equals(LiveMonitorType.PERIOD_MONITOR)) {
            periodEventMonitor();
        } else {
            individualEventMonitor();
        }
    }

    @Override
    public void stop() {
        clearMem();
    }

    @Override
    public void clearMem() {
        if (customerEventListener == null || vendorEventListener == null) {
            return;
        }
        publisher.unsubscribe(CustomerEvent.class, this.customerEventListener);
        publisher.unsubscribe(VendorEvent.class, this.vendorEventListener);
    }
}
