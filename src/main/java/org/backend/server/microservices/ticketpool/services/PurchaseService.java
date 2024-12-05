package org.backend.server.microservices.ticketpool.services;

import jakarta.persistence.EntityNotFoundException;
import org.backend.server.microservices.ticketpool.enums.PurchaseStatus;
import org.backend.server.microservices.ticketpool.models.Purchase;
import org.backend.server.microservices.ticketpool.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public void savePendingPurchase(Purchase purchase) {
        purchase.setPurchaseStatus(PurchaseStatus.PENDING);
        purchaseRepository.save(purchase);
    }

    public Purchase findPendingPurchase(long id) {
        return purchaseRepository.findByIdAndPurchaseStatus(id, PurchaseStatus.PENDING);
    }

    public List<Purchase> findAllExpiredPendingPurchases() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(5);
        return purchaseRepository.findByPurchaseStatusAndCreatedAtAfter(PurchaseStatus.PENDING, Date.from(cutoffTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    public void removeExpiredPendingPurchases(Purchase purchase) {
        purchaseRepository.delete(purchase);
    }

    public void savePurchase(Purchase purchase) {
        Purchase pendingPurchase = findPendingPurchase(purchase.getId());
        if (purchase.getPurchaseStatus() != PurchaseStatus.PENDING) {
            throw new EntityNotFoundException("Purchase status is not PENDING");
        }
        pendingPurchase.setPurchaseDate(new Date());
        pendingPurchase.setPurchaseStatus(PurchaseStatus.PURCHASED);
        purchaseRepository.save(purchase);
    }

    public Purchase findPurchase(long id) {
        return purchaseRepository.findByIdAndPurchaseStatus(id, PurchaseStatus.PURCHASED);
    }
}
