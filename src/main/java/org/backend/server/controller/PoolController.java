package org.backend.server.controller;

import org.backend.dto.PurchaseDto;
import org.backend.dto.TicketDto;
import org.backend.enums.PurchaseStatus;
import org.backend.model.Purchase;
import org.backend.model.Ticket;
import org.backend.pools.PurchasePool;
import org.backend.pools.ThreadPool;
import org.backend.pools.TicketPool;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pool")
public class PoolController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ThreadPool threadPool;
    private final TicketPool ticketPool;
    private final PurchasePool purchasePool;

    public PoolController(SimpMessagingTemplate simpMessagingTemplate, ThreadPool threadPool, TicketPool ticketPool, PurchasePool purchasePool) {
        this.messagingTemplate = simpMessagingTemplate;
        this.threadPool = threadPool;
        this.ticketPool = ticketPool;
        this.purchasePool = purchasePool;
    }

    @GetMapping("/ticket/all")
    public List<TicketDto> allTickets(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int skip) {
        return ticketPool.getInUseObjects().stream().map(object -> (Ticket) object).map(Ticket::toDto).skip(skip).limit(limit).toList();
    }

    @GetMapping("/ticket/quantity-not-full")
    public List<TicketDto> quantityNotFullTickets() {
        return ticketPool.findAllQuantityNotFullTicket().stream().map(Ticket::toDto).toList();
    }

    @GetMapping("/purchase/all")
    public List<PurchaseDto> allPurchases(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int skip) {
        return purchasePool.getInUseObjects().stream().map(object -> (Purchase) object).map(Purchase::toDto).skip(skip).limit(limit).toList();
    }

    @GetMapping("/purchase/status-pending")
    public List<PurchaseDto> statusPendingPurchases(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int skip) {
        return purchasePool.getInUseObjects().stream().map(object -> (Purchase) object).filter(purchase -> purchase.getPurchaseStatus().equals(PurchaseStatus.PENDING)).map(Purchase::toDto).skip(skip).limit(limit).toList();
    }

    @GetMapping("/purchase/status-purchased")
    public List<PurchaseDto> statusPurchasedPurchases(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int skip) {
        return purchasePool.getInUseObjects().stream().map(object -> (Purchase) object).filter(purchase -> purchase.getPurchaseStatus().equals(PurchaseStatus.PURCHASED)).map(Purchase::toDto).skip(skip).limit(limit).toList();
    }

    @Scheduled(fixedDelay = 2000)  // 10 seconds interval
    public void sendPoolSummery() {
        Map<String, Integer> poolSummery = new HashMap<>();
        poolSummery.put("threadPoolSize", threadPool.getInUseObjects().size());
        poolSummery.put("ticketPoolSize", ticketPool.getInUseObjects().size());
        poolSummery.put("purchasePoolSize", purchasePool.getInUseObjects().size());
        messagingTemplate.convertAndSend("/summery/pool", poolSummery);
    }
}
