package org.backend.pools;

import lombok.Getter;
import org.backend.enums.PurchaseStatus;
import org.backend.model.Customer;
import org.backend.model.Purchase;
import org.backend.model.Ticket;
import org.backend.model.Vendor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class TicketPool extends PoolAbstract {
    private final PurchasePool purchasePool;

    public TicketPool(int poolMaxCapacity, PurchasePool purchasePool) {
        super(poolMaxCapacity);
        this.purchasePool = purchasePool;
    }

    public String addTicket(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        if (isPoolFull()) {
            return null;
        }
        increasePoolUsedCapacity(ticket);
        return ticket.getId();
    }

    public Ticket findTicket(String id) {
        for (Object obj : inUseObjects) {
            Ticket ticket = (Ticket) obj;
            if (ticket.getId().equals(id) && !ticket.isDeleted()) {
                return ticket;
            }
        }
        return null;
    }

    public List<Ticket> findAllQuantityNotFullTicket() {
        List<Ticket> ticketsQuantityNotFull = new ArrayList<Ticket>();
        for (Object obj : inUseObjects) {
            Ticket ticket = (Ticket) obj;
            if (!ticket.isDeleted() && !ticket.isBoughtQuantityReachedMaxQuantity()) {
                ticketsQuantityNotFull.add(ticket);
            }
        }
        return ticketsQuantityNotFull;
    }

    public boolean removeQuantityFullTickets(Ticket ticket, Vendor vendor) {
        if (ticket == null || !ticket.getVendor().equals(vendor) || !ticket.isBoughtQuantityReachedMaxQuantity()) {
            return false;
        }
        return ticket.lockAndExecute(() -> {
            if (ticket.isDeleted()) {
                return false;
            }
            ticket.deleted();
            decreasePoolUsedCapacity(ticket);
            return true;
        });
    }

    public boolean removeAllTickets(Ticket ticket, Vendor vendor) {
        if (ticket == null || !ticket.getVendor().equals(vendor)) {
            return false;
        }
        return ticket.lockAndExecute(() -> {
            if (ticket.isDeleted()) {
                return false;
            }
            ticket.deleted();
            decreasePoolUsedCapacity(ticket);
            return true;
        });
    }

    public Purchase queTicket(String id, Customer customer) {
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
            purchasePool.addPurchase(purchase);
            ticket.increaseBoughtQuantity();
            return purchase;
        });
    }

    public Purchase buyTicket(String id, Customer customer) {
        Purchase purchase = purchasePool.findPurchase(id);
        if (purchase == null || !purchase.getCustomer().equals(customer)) {
            return null;
        }
        return purchase.lockAndExecute(() -> {
            if (!purchase.getPurchaseStatus().equals(PurchaseStatus.PENDING)) {
                return null;
            }
            purchase.setPurchaseDate(new Date());
            purchase.setPurchaseStatus(PurchaseStatus.PURCHASED);
            return purchase;
        });
    }
}
