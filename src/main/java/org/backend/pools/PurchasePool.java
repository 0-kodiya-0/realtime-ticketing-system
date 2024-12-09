package org.backend.pools;

import lombok.Getter;
import org.backend.enums.PurchaseStatus;
import org.backend.model.Customer;
import org.backend.model.Purchase;

import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class PurchasePool {
    private final ConcurrentLinkedQueue<Purchase> inUsePurchaseHistory;

    public PurchasePool() {
        this.inUsePurchaseHistory = new ConcurrentLinkedQueue<>();
    }

    public String addPurchase(Purchase purchase) {
        if (purchase == null) {
            return null;
        }
        purchase.setPurchaseStatus(PurchaseStatus.PENDING);
        inUsePurchaseHistory.add(purchase);
        return purchase.getId();
    }

    public Purchase findPurchase(String id) {
        for (Purchase purchase : inUsePurchaseHistory) {
            if (purchase.getId().equals(id)) {
                return purchase;
            }
        }
        return null;
    }

    public boolean removePurchase(String id, Customer customer) {
        Purchase purchase = findPurchase(id);
        if (purchase == null) {
            return false;
        }
        if (!purchase.getCustomer().equals(customer) && purchase.getPurchaseStatus() == PurchaseStatus.PURCHASED) {
            return false;
        }
        inUsePurchaseHistory.remove(purchase);
        return true;
    }
}
