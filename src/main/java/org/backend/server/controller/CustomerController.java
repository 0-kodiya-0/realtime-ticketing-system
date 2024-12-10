package org.backend.server.controller;

import org.backend.dto.CustomerDto;
import org.backend.dto.PurchaseDto;
import org.backend.model.Customer;
import org.backend.model.Purchase;
import org.backend.pools.PurchasePool;
import org.backend.pools.ThreadPool;
import org.backend.services.CustomerSimulation;
import org.backend.thread.ThreadExecutableAbstract;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final ThreadPool threadPool;
    private final PurchasePool purchasePool;

    public CustomerController(ThreadPool threadPool, PurchasePool purchasePool) {
        this.threadPool = threadPool;
        this.purchasePool = purchasePool;
    }

    @GetMapping("/all-customer")
    public List<CustomerDto> allCustomers() {
        return threadPool.findTargetClassThread(CustomerSimulation.class).stream().map(CustomerSimulation::getCustomer).map(Customer::toDto).toList();
    }

    @GetMapping("/active-customer")
    public List<CustomerDto> activeCustomers() {
        return threadPool.findTargetClassThread(CustomerSimulation.class).stream().filter(ThreadExecutableAbstract::isRunning).map(CustomerSimulation::getCustomer).map(Customer::toDto).toList();
    }

    @GetMapping("/purchase/{:id}")
    public List<PurchaseDto> allPurchases(@PathVariable String id) {
        return purchasePool.findPurchaseForCustomer(id).stream().map(Purchase::toDto).toList();
    }
}
