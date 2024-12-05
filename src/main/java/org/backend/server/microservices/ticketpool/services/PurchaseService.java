package org.backend.server.microservices.ticketpool.services;

import org.backend.server.microservices.ticketpool.enums.PurchaseStatus;
import org.backend.server.microservices.ticketpool.models.Purchase;
import org.backend.server.microservices.ticketpool.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final ArrayList<Purchase> purchasePendingPool = new ArrayList<>();

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public void addPendingPurchase(Purchase purchase) {
        purchasePendingPool.add(purchase);
    }

    public Purchase getPendingPurchase(Long purchaseId) {
        for (Purchase purchase : purchasePendingPool) {
            if (purchase.getPurchaseId().equals(purchaseId)) {
                return purchase;
            }
        }
        return null;
    }

    public void addPurchase(Purchase purchase) {
        if (purchase.getPurchaseStatus() != PurchaseStatus.PENDING) {
            throw new IllegalArgumentException("Purchase status is not PENDING");
        }
        purchase.setPurchaseStatus(PurchaseStatus.PURCHASED);
        purchaseRepository.save(purchase);
    }

    public Optional<Purchase> getPurchase(Long purchaseId) {
        return purchaseRepository.findById(purchaseId);
    }

    public void removePendingPurchase(Purchase purchase) {
        purchasePendingPool.remove(purchase);
    }
}
