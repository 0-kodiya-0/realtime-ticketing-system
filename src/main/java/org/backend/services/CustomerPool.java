package org.backend.services;

import lombok.Getter;
import org.backend.model.Customer;
import org.backend.model.Purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class CustomerPool extends PoolAbstract {
    private final ConcurrentLinkedQueue<Customer> inUseCustomers;
    private final PurchasePool purchasePool;

    public CustomerPool(int poolMaxCapacity, PurchasePool purchasePool) {
        super(poolMaxCapacity);
        this.purchasePool = purchasePool;
        this.inUseCustomers = new ConcurrentLinkedQueue<>();
    }

    public String addCustomer(Customer customer) {
        if (customer == null) {
            return null;
        }
        if (isPoolFull()) {
//            System.out.println("Customer pool full");
            return null;
        }
        inUseCustomers.add(customer);
        increasePoolUsedCapacity();
        return customer.getId();
    }

    public Customer findCustomer(String id) {
        for (Customer customer : inUseCustomers) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }

    public boolean removeCustomer(String id) {
        Customer customer = findCustomer(id);
        if (customer == null) {
            return false;
        }
        inUseCustomers.remove(customer);
        decreasePoolUsedCapacity();
        return true;
    }

    public List<Purchase> removeCustomerPurchases(String id) {
        Customer customer = findCustomer(id);
        if (customer == null) {
            return null;
        }
        List<Purchase> removedPurchases = new ArrayList<>();
        for (Purchase purchase : purchasePool.getInUsePurchaseHistory()) {
            if (purchasePool.removePurchase(purchase.getId(), customer)) {
                removedPurchases.add(purchase);
            }
        }
        return removedPurchases;
    }
}
