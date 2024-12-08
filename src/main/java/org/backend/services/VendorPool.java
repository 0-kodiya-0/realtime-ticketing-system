package org.backend.services;

import lombok.Getter;
import org.backend.model.Ticket;
import org.backend.model.Vendor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class VendorPool extends PoolAbstract {
    private final ConcurrentLinkedQueue<Vendor> inUseVendor;
    private final TicketPool ticketPool;

    public VendorPool(int poolMaxCapacity, TicketPool ticketPool) {
        super(poolMaxCapacity);
        this.ticketPool = ticketPool;
        this.inUseVendor = new ConcurrentLinkedQueue<>();
    }

    public String addVendor(Vendor vendor) {
        if (vendor == null) {
            return null;
        }
        if (isPoolFull()) {
//            System.out.println("Vendor pool full");
            return null;
        }
        inUseVendor.add(vendor);
        increasePoolUsedCapacity();
        return vendor.getId();
    }

    public Vendor findVendor(String id) {
        for (Vendor vendor : inUseVendor) {
            if (vendor.getId().equals(id) && !vendor.isDeleted()) {
                return vendor;
            }
        }
        return null;
    }

    public boolean removeVendor(String id) {
        Vendor vendor = findVendor(id);
        if (vendor == null) {
            return false;
        }
        inUseVendor.remove(vendor);
        vendor.deleted();
        decreasePoolUsedCapacity();
        return true;
    }

    public List<Ticket> removeVendorTickets(String id) {
        Vendor vendor = findVendor(id);
        if (vendor == null) {
            return null;
        }
        List<Ticket> removedTickets = new ArrayList<>();
        for (Ticket ticket : ticketPool.getInUseTickets()) {
            if (ticketPool.removeTicket(ticket.getId(), vendor)) {
                removedTickets.add(ticket);
            }
        }
        return removedTickets;
    }
}
