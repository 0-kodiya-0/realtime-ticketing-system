package org.backend.pools;

import lombok.Getter;
import org.backend.enums.PurchaseStatus;
import org.backend.model.Customer;
import org.backend.model.Purchase;
import org.backend.model.Ticket;
import org.backend.model.Vendor;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class TicketPool extends PoolAbstract {
    private final ConcurrentLinkedQueue<Ticket> inUseTickets;
    private final PurchasePool purchasePool;

    public TicketPool(int poolMaxCapacity, PurchasePool purchasePool) {
        super(poolMaxCapacity);
        this.inUseTickets = new ConcurrentLinkedQueue<>();
        this.purchasePool = purchasePool;
    }

    public String addTicket(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        if (isPoolFull()) {
            return null;
        }
        inUseTickets.add(ticket);
        increasePoolUsedCapacity();
        return ticket.getId();
    }

    public Ticket findTicket(String id) {
        for (Ticket ticket : inUseTickets) {
            if (ticket.getId().equals(id) && !ticket.isDeleted()) {
                return ticket;
            }
        }
        return null;
    }

    public boolean removeQuantityFullTickets(String id, Vendor vendor) {
        Ticket ticket = findTicket(id);
        if (ticket == null || !ticket.getVendor().equals(vendor) || !ticket.isBoughtQuantityReachedMaxQuantity()) {
            return false;
        }
        return ticket.lockAndExecute(() -> {
            if (ticket.isDeleted()) {
                return false;
            }
            inUseTickets.remove(ticket);
            ticket.deleted();
            decreasePoolUsedCapacity();
            return true;
        });
    }

    public boolean removeAllTickets(String id, Vendor vendor) {
        Ticket ticket = findTicket(id);
        if (ticket == null || !ticket.getVendor().equals(vendor)) {
            return false;
        }
        return ticket.lockAndExecute(() -> {
            if (ticket.isDeleted()) {
                return false;
            }
            inUseTickets.remove(ticket);
            ticket.deleted();
            decreasePoolUsedCapacity();
            return true;
        });
    }

    public String queTicket(String id, Customer customer) {
        Ticket ticket = findTicket(id);
        if (ticket == null) {
            return null;
        }
        return ticket.lockAndExecute(() -> {
            if (ticket.isDeleted()) {
                return null;
            }
            Purchase purchase = new Purchase(ticket, customer);
            if (ticket.isBoughtQuantityReachedMaxQuantity()) {
                return null;
            }
            String purchaseId = purchasePool.addPurchase(purchase);
            ticket.increaseBoughtQuantity();
            return purchaseId;
        });
    }

    public boolean buyTicket(String id, Customer customer) {
        Purchase purchase = purchasePool.findPurchase(id);
        if (purchase == null || !purchase.getCustomer().equals(customer)) {
            return false;
        }
        return purchase.lockAndExecute(() -> {
            if (!purchase.getPurchaseStatus().equals(PurchaseStatus.PENDING)) {
                return false;
            }
            purchase.setPurchaseDate(new Date());
            purchase.setPurchaseStatus(PurchaseStatus.PURCHASED);
            return true;
        });
    }
}
