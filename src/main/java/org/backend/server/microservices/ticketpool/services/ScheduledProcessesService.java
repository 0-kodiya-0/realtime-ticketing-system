package org.backend.server.microservices.ticketpool.services;

import org.backend.server.microservices.ticketpool.models.Purchase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledProcessesService {

    private final TicketService ticketService;
    private final PurchaseService purchaseService;

    public ScheduledProcessesService(TicketService ticketService, PurchaseService purchaseService) {
        this.ticketService = ticketService;
        this.purchaseService = purchaseService;
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    protected void cleanupPendingPurchases() {
        List<Purchase> expiredPendingPurchases = purchaseService.findAllExpiredPendingPurchases();
        expiredPendingPurchases.forEach(purchase -> {
            ticketService.updateTicketBoughtQuantity(purchase.getTicket().getId(), false);
            purchaseService.removeExpiredPendingPurchases(purchase);
        });
    }
}
