package org.backend.pools;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.backend.enums.PurchaseStatus;
import org.backend.model.Customer;
import org.backend.model.Purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchasePool extends PoolAbstract {

    public PurchasePool() {}

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

    public List<Purchase> findPurchaseForCustomer(String customerId) {
        List<Purchase> purchaseList = new ArrayList<>();
        for (Object obj : inUseObjects) {
            Purchase purchase = (Purchase) obj;
            if (purchase.getCustomer().getId().equals(customerId)) {
                purchaseList.add(purchase);
            }
        }
        return purchaseList;
    }

    public boolean removePurchase(Purchase purchase, Customer customer) {
        if (purchase == null) {
            return false;
        }
        if (!purchase.getCustomer().equals(customer) || purchase.getPurchaseStatus().equals(PurchaseStatus.PENDING)) {
            return false;
        }
        decreasePoolUsedCapacity(purchase);
        return true;
    }
}
