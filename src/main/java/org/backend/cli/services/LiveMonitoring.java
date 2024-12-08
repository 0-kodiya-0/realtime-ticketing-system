package org.backend.cli.services;

import org.backend.cli.enums.LiveMonitorType;
import org.backend.enums.EventTypes;
import org.backend.event.*;
import org.backend.services.CustomerPool;
import org.backend.services.PurchasePool;
import org.backend.services.TicketPool;
import org.backend.services.VendorPool;

import java.util.Date;
import java.util.List;

public class LiveMonitoring implements Simulation {
    private final TicketPool ticketPool;
    private final PurchasePool purchasePool;
    private final VendorPool vendorPool;
    private final CustomerPool customerPool;
    private final EventPublisher publisher = EventPublisher.getInstance();
    private final LiveMonitorType liveMonitoringType;
    private ThreadEventPasser threadEventPasser;
    private List<EventListener<? extends Event>> eventListeners;

    public LiveMonitoring(LiveMonitorType liveMonitoringType, ThreadEventPasser threadEventPasser, TicketPool ticketPool, PurchasePool purchasePool, VendorPool vendorPool, CustomerPool customerPool) {
        this.liveMonitoringType = liveMonitoringType;
        this.threadEventPasser = threadEventPasser;
        this.ticketPool = ticketPool;
        this.purchasePool = purchasePool;
        this.vendorPool = vendorPool;
        this.customerPool = customerPool;
    }

    public List<EventListener<? extends Event>> individualEventMonitor() {
        EventListener<CustomerEvent> customerEventEventListener = new EventListener<CustomerEvent>() {
            @Override
            public void onEvent(CustomerEvent event) {
                System.out.println(event);
            }
        };
        EventListener<VendorEvent> vendorEventEventListener = new EventListener<VendorEvent>() {

            @Override
            public void onEvent(VendorEvent event) {
                System.out.println(event);
            }
        };
        publisher.subscribe(CustomerEvent.class, customerEventEventListener);
        publisher.subscribe(VendorEvent.class, vendorEventEventListener);
        return List.of(customerEventEventListener, vendorEventEventListener);
    }

    private void periodEventMonitor() {
        Date liveMonitorStartTime = new Date();
        int customerPoolBeforeCount = customerPool.getInUseCustomers().size();
        int purchasePoolBeforeCount = purchasePool.getInUsePurchaseHistory().size();
        int ticketPoolBeforeCount = ticketPool.getInUseTickets().size();
        int vendorPoolBeforeCount = vendorPool.getInUseVendor().size();
        while (!threadEventPasser.receiveEvent().equals(EventTypes.REMOVED_THREAD)) {
            try {
                Date liveMonitorEndTime = new Date();
                System.out.println("------------------------------------------------------------");
                System.out.println("Live monitor status from (" + liveMonitorStartTime + ") to (" + liveMonitorEndTime + ")");
                System.out.println(" Customer pool size : " + customerPool.getInUseCustomers().size());
                System.out.println(" Purchase pool size : " + purchasePool.getInUsePurchaseHistory().size());
                System.out.println(" Ticket pool size   : " + ticketPool.getInUseTickets().size());
                System.out.println(" Vendor pool size   : " + vendorPool.getInUseVendor().size());
                System.out.println("---------------------");
                int newlyAddedCustomers = customerPool.getInUseCustomers().size() - customerPoolBeforeCount;
                if (newlyAddedCustomers > 0) {
                    System.out.println(" * " + newlyAddedCustomers + " new customers added");
                } else {
                    System.out.println(" * " + -newlyAddedCustomers + " customers removed");
                }
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
                int newlyAddedVendors = vendorPool.getInUseVendor().size() - vendorPoolBeforeCount;
                if (newlyAddedVendors > 0) {
                    System.out.println(" * " + newlyAddedVendors + " new vendors added");
                } else {
                    System.out.println(" * " + -newlyAddedVendors + " vendors removed");
                }
                customerPoolBeforeCount = customerPool.getInUseCustomers().size();
                purchasePoolBeforeCount = purchasePool.getInUsePurchaseHistory().size();
                ticketPoolBeforeCount = ticketPool.getInUseTickets().size();
                vendorPoolBeforeCount = vendorPool.getInUseVendor().size();
                liveMonitorStartTime = liveMonitorEndTime;
                Thread.sleep(3000);
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
        start();
        clearMem();
    }

    @Override
    public void start() {
        if (liveMonitoringType.equals(LiveMonitorType.PERIOD_MONITOR)) {
            periodEventMonitor();
        } else {
            eventListeners = individualEventMonitor();
        }
    }

    @Override
    public void clearMem() {
        if (eventListeners == null) {
            return;
        }
        for (EventListener listener : eventListeners) {
            publisher.unsubscribe(listener.getClass(), listener);
        }
    }
}
