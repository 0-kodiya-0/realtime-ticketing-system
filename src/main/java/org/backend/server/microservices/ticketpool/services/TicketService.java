package org.backend.server.microservices.ticketpool.services;

import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.services.CustomerService;
import org.backend.server.microservices.ticketpool.enums.PurchaseStatus;
import org.backend.server.microservices.ticketpool.models.Purchase;
import org.backend.server.microservices.ticketpool.models.Ticket;
import org.backend.server.microservices.ticketpool.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final CustomerService customerService;
    private final TicketRepository ticketRepository;
    private final PurchaseService purchaseService;

    public TicketService(TicketRepository ticketRepository, PurchaseService purchaseService, CustomerService customerService) {
        this.ticketRepository = ticketRepository;
        this.purchaseService = purchaseService;
        this.customerService = customerService;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAllByVisibleTrueAndDeletedFalseOrderByCreatedAtAsc();
    }

    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findByTicketIdAndVisibleTrue(ticketId).orElse(null);
    }

    public boolean checkTicketBoughtQuantityExceeded(Long ticketId) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }
        return ticket.getQuantity() <= ticket.getBoughtQuantity();
    }

    public void addTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public void removeTicket(Long id) {
        Ticket ticket = getTicket(id);
        ticket.setDeleted(true);
        ticketRepository.save(ticket);
    }

    public void updateTicketBoughtQuantity(Ticket ticket) {
        ticket.setBoughtQuantity(ticket.getBoughtQuantity() + ticket.getQuantity());
        ticketRepository.save(ticket);
    }

    @Transactional
    public Purchase queTicket(Long ticketId, String customerId) {
        Customer customer = customerService.findCustomer(customerId);
        Purchase pendingPurchase = queTicket(ticketId);
        pendingPurchase.setCustomer(customer);
        pendingPurchase.setPurchaseStatus(PurchaseStatus.PENDING);
        purchaseService.addPendingPurchase(pendingPurchase);
        return pendingPurchase;
    }

    @Transactional
    public Purchase queTicket(Long ticketId, Customer customer) {
        Purchase pendingPurchase = queTicket(ticketId);
        pendingPurchase.setCustomer(customer);
        pendingPurchase.setPurchaseStatus(PurchaseStatus.PENDING);
        purchaseService.addPendingPurchase(pendingPurchase);
        return pendingPurchase;
    }

    private Purchase queTicket(Long ticketId) {
        Purchase pendingPurchase = new Purchase();
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found");
        }
        if (ticket.getQuantity() <= ticket.getBoughtQuantity()) {
            throw new RuntimeException("Ticket not available to be bought");
        }
        pendingPurchase.setTicket(ticket);
        return pendingPurchase;
    }

    @Transactional
    public Purchase purchaseTicket(Long purchaseId, Long customerId) {
        Purchase pendingPurchase = purchaseService.getPendingPurchase(purchaseId);
        if (pendingPurchase == null) {
            throw new RuntimeException("Pending purchase not found");
        }
        if (pendingPurchase.getPurchaseStatus() != PurchaseStatus.PENDING || !(pendingPurchase.getCustomer().getId() == customerId)) {
            throw new RuntimeException("Pending purchase not accepted");
        }
        return purchaseTicket(purchaseId, pendingPurchase);
    }

    @Transactional
    public Purchase purchaseTicket(Long purchaseId, Customer customer) {
        Purchase pendingPurchase = purchaseService.getPendingPurchase(purchaseId);
        if (pendingPurchase == null) {
            throw new RuntimeException("Pending purchase not found");
        }
        if (pendingPurchase.getPurchaseStatus() != PurchaseStatus.PENDING || !(pendingPurchase.getCustomer().getId() == customer.getId())) {
            throw new RuntimeException("Pending purchase not accepted");
        }
        return purchaseTicket(purchaseId, pendingPurchase);
    }

    private Purchase purchaseTicket(Long purchaseId, Purchase pendingPurchase) {
        if (checkTicketBoughtQuantityExceeded(pendingPurchase.getTicket().getTicketId())) {
            throw new RuntimeException("Ticket purchase count exceeded");
        }
        purchaseService.addPurchase(pendingPurchase);
        Optional<Purchase> savePurchase = purchaseService.getPurchase(purchaseId);
        if (savePurchase.isEmpty()) {
            throw new RuntimeException("Saved purchase not found");
        }
        purchaseService.removePendingPurchase(pendingPurchase);
        updateTicketBoughtQuantity(savePurchase.get().getTicket());
        return savePurchase.get();
    }
}
