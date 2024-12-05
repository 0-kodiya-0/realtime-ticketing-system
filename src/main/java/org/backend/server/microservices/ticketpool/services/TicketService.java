package org.backend.server.microservices.ticketpool.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.backend.server.microservices.authorization.models.Customer;
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

    public TicketService(TicketRepository ticketRepository, PurchaseService purchaseService) {
        this.ticketRepository = ticketRepository;
        this.purchaseService = purchaseService;
    }

    public List<Ticket> findAllTickets() {
        return ticketRepository.findAllByVisibleAndDeleted(true, false);
    }

    public Ticket findTicket(Long ticketId) {
        return ticketRepository.findByIdAndVisibleAndDeleted(ticketId, true, false);
    }

    public void saveTicket(Ticket ticket) {
        ticket.setVisible(true);
        ticketRepository.save(ticket);
    }

    public void removeTicket(Long id) {
        Ticket ticket = findTicket(id);
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
    public Purchase queTicket(Long ticketId, Customer customer) {
        Purchase pendingPurchase = queTicket(ticketId);
        pendingPurchase.setCustomer(customer);
        pendingPurchase.setPurchaseStatus(PurchaseStatus.PENDING);
        purchaseService.savePendingPurchase(pendingPurchase);
        return pendingPurchase;
    }

    @Transactional
    protected Purchase queTicket(Long ticketId) {
        Purchase pendingPurchase = new Purchase();
        Ticket ticket = findTicket(ticketId);
        if (ticket == null) {
            throw new EntityNotFoundException("Ticket not found");
        }
        if (ticket.getQuantity() <= ticket.getBoughtQuantity()) {
            throw new IllegalArgumentException("Ticket not available to be bought");
        }
        pendingPurchase.setTicket(ticket);
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
        return purchaseTicket(purchaseId, pendingPurchase);
    }

    @Transactional
    protected Purchase purchaseTicket(Long purchaseId, Purchase pendingPurchase) {
        if (checkTicketBoughtQuantityExceeded(pendingPurchase.getTicket().getId())) {
            throw new IllegalArgumentException("Ticket purchase count exceeded");
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
