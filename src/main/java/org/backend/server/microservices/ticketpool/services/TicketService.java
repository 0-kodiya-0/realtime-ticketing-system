package org.backend.server.microservices.ticketpool.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.authorization.services.VendorService;
import org.backend.server.microservices.ticketpool.enums.PurchaseStatus;
import org.backend.server.microservices.ticketpool.models.Purchase;
import org.backend.server.microservices.ticketpool.models.Ticket;
import org.backend.server.microservices.ticketpool.repository.TicketRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final PurchaseService purchaseService;
    private final VendorService vendorService;

    public TicketService(TicketRepository ticketRepository, PurchaseService purchaseService, VendorService vendorService) {
        this.ticketRepository = ticketRepository;
        this.purchaseService = purchaseService;
        this.vendorService = vendorService;
    }

    public List<Ticket> findAllTickets() {
        return ticketRepository.findAllByVisibleAndDeleted(true, false);
    }

    public Ticket findTicket(Long ticketId) {
        return ticketRepository.findByIdAndVisibleAndDeleted(ticketId, true, false);
    }

    public void saveTicket(Customer customer, Ticket ticket) {
        Vendor vendor = vendorService.findVendor(customer.getId());
        ticket.setVendor(vendor);
        ticket.setVisible(true);
        ticketRepository.save(ticket);
    }

    public void removeTicket(Customer customer, Long id) {
        Vendor vendor = vendorService.findVendor(customer.getId());
        Ticket ticket = ticketRepository.findByIdAndVendorAndVisibleAndDeleted(id, vendor, true, false);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket with id " + id + " not found or do not belong to you");
        }
        ticket.setDeleted(true);
        ticketRepository.save(ticket);
    }

    public boolean checkTicketBoughtQuantityExceeded(Long ticketId) {
        Ticket ticket = findTicket(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        return ticket.getQuantity() <= ticket.getBoughtQuantity();
    }

    @Transactional(timeout = 10)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void updateTicketBoughtQuantity(long id, boolean increaseBoughtQuantity) {
        Ticket ticket = findTicket(id);
        synchronized (ticket) {
            if (increaseBoughtQuantity) {
                ticket.setBoughtQuantity(ticket.getBoughtQuantity() + 1);
            } else {
                ticket.setBoughtQuantity(ticket.getBoughtQuantity() - 1);
            }
            ticketRepository.save(ticket);
        }
    }

    @Transactional
    public Purchase queTicket(Customer customer, Long ticketId) {
        Purchase pendingPurchase = new Purchase();
        Ticket ticket = findTicket(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        if (ticket.getQuantity() <= ticket.getBoughtQuantity()) {
            throw new IllegalArgumentException("Ticket not available to be bought");
        }
        pendingPurchase.setTicket(ticket);
        pendingPurchase.setCustomer(customer);
        pendingPurchase.setPurchaseStatus(PurchaseStatus.PENDING);
        purchaseService.savePendingPurchase(pendingPurchase);
        updateTicketBoughtQuantity(ticket.getId(), true);
        return pendingPurchase;
    }

    @Transactional
    public Purchase purchaseTicket(Long purchaseId, Customer customer) throws IllegalAccessException {
        Purchase pendingPurchase = purchaseService.findPendingPurchase(purchaseId);
        if (pendingPurchase == null) {
            throw new EntityNotFoundException("Pending purchase not found");
        }
        if (pendingPurchase.getPurchaseStatus() != PurchaseStatus.PENDING || !(pendingPurchase.getCustomer().getId() == customer.getId())) {
            throw new IllegalAccessException("Pending purchase not accepted");
        }
        purchaseService.savePurchase(pendingPurchase);
        Purchase savePurchase = purchaseService.findPurchase(purchaseId);
        if (savePurchase == null) {
            throw new EntityNotFoundException("Saved purchase not found");
        }
        updateTicketBoughtQuantity(savePurchase.getTicket().getId(), true);
        return savePurchase;
    }
}
