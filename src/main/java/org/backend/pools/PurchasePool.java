package org.backend.pools;

import lombok.Getter;
import org.backend.enums.PurchaseStatus;
import org.backend.model.Customer;
import org.backend.model.Purchase;

import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class PurchasePool extends PoolAbstract {

    public PurchasePool(int poolMaxCapacity) {
        super(poolMaxCapacity);
    }

    public String addPurchase(Purchase purchase) {
        if (purchase == null) {
            return null;
        }
        purchase.setPurchaseStatus(PurchaseStatus.PENDING);
        increasePoolUsedCapacity(purchase);
        return purchase.getId();
    }

    public Purchase findPurchase(String id) {
        for (Object obj : inUseObjects) {
            Purchase purchase = (Purchase) obj;
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
        decreasePoolUsedCapacity(purchase);
        return true;
    }
}
