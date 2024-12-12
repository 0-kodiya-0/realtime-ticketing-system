package org.backend.server.controller;

import org.backend.dto.*;
import org.backend.enums.CustomerTypes;
import org.backend.enums.FilePaths;
import org.backend.io.file.JsonReader;
import org.backend.model.Customer;
import org.backend.model.Purchase;
import org.backend.model.Ticket;
import org.backend.model.Vendor;
import org.backend.pools.PurchasePool;
import org.backend.pools.ThreadPool;
import org.backend.pools.TicketPool;
import org.backend.simulation.CustomerSimulation;
import org.backend.simulation.ThreadExecutable;
import org.backend.simulation.ThreadExecutableAbstract;
import org.backend.simulation.VendorSimulation;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages and controls the overall simulation lifecycle for the ticket system through http.
 * Provides methods for checking simulation status, starting and stopping the
 * simulation execution. Acts as the main controller for coordinating simulation
 * components.
 */
@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimpMessagingTemplate messagingTemplate;

    private final ThreadPool threadPool;
    private final TicketPool ticketPool;
    private final PurchasePool purchasePool;
    private final MainConfigurationDto mainConfigurationDto;

    public SimulationController(SimpMessagingTemplate messagingTemplate, ThreadPool threadPool, TicketPool ticketPool, PurchasePool purchasePool, MainConfigurationDto mainConfigurationDto) {
        this.messagingTemplate = messagingTemplate;
        this.threadPool = threadPool;
        this.ticketPool = ticketPool;
        this.purchasePool = purchasePool;
        this.mainConfigurationDto = mainConfigurationDto;
    }

    @GetMapping("/is-running")
    public boolean isRunning() {
        return !threadPool.getActiveThreads().isEmpty();
    }

    @GetMapping("/start")
    public int startSimulation() {
        return threadPool.startNotRunningThreads(CustomerSimulation.class) + threadPool.startNotRunningThreads(VendorSimulation.class);
    }

    @GetMapping("/stop")
    public int stopSimulation() {
        return threadPool.stopRunningThreads(CustomerSimulation.class) + threadPool.stopRunningThreads(VendorSimulation.class);
    }

    @GetMapping("/customer/all")
    public List<CustomerDto> allCustomers(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int skip) {
        return threadPool.findTargetClassThread(CustomerSimulation.class).stream().map(CustomerSimulation::getCustomer).map(Customer::toDto).skip(skip).limit(limit).toList();
    }

    @GetMapping("/customer/start")
    public int startCustomerSimulation() {
        return threadPool.startNotRunningThreads(CustomerSimulation.class);
    }

    @GetMapping("/customer/stop")
    public int stopCustomerSimulation() {
        return threadPool.stopRunningThreads(CustomerSimulation.class);
    }

    @PostMapping("/customer/add")
    public int addCustomerSimulation(@RequestParam boolean isVip, @RequestParam int repetitionCount) {
        return threadPool.addRunnable(() -> {
            return new CustomerSimulation(mainConfigurationDto.getTicketRetrievalRate(), isVip ? CustomerTypes.VIP : CustomerTypes.NOT_VIP, ticketPool, purchasePool);
        }, repetitionCount);
    }

    @GetMapping("/customer/active")
    public List<CustomerDto> activeCustomers() {
        return threadPool.findTargetClassThread(CustomerSimulation.class).stream().filter(ThreadExecutableAbstract::isRunning).map(CustomerSimulation::getCustomer).map(Customer::toDto).toList();
    }

    @GetMapping("/customer/purchase/{:id}")
    public List<PurchaseDto> allPurchases(@PathVariable String id) {
        return purchasePool.findPurchaseForCustomer(id).stream().map(Purchase::toDto).toList();
    }

    @GetMapping("/vendor/all")
    public List<VendorDto> allVendor(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int skip) {
        return threadPool.findTargetClassThread(VendorSimulation.class).stream().map(VendorSimulation::getVendor).map(Vendor::toDto).skip(skip).limit(limit).toList();
    }

    @GetMapping("/vendor/start")
    public int startVendorSimulation() {
        return threadPool.startNotRunningThreads(VendorSimulation.class);
    }

    @GetMapping("/vendor/stop")
    public int stopVendorSimulation() {
        return threadPool.stopRunningThreads(VendorSimulation.class);
    }

    @PostMapping("/vendor/add")
    public int addVendorSimulation(@RequestParam int repetitionCount) {
        return threadPool.addRunnable(() -> {
            return new VendorSimulation(mainConfigurationDto.getTicketReleaseRate(), ticketPool);
        }, repetitionCount);
    }

    @GetMapping("/vendor/active")
    public List<VendorDto> activeVendor() {
        return threadPool.findTargetClassThread(VendorSimulation.class).stream().filter(ThreadExecutableAbstract::isRunning).map(VendorSimulation::getVendor).map(Vendor::toDto).toList();
    }

    @GetMapping("/vendor/ticket-active/{:id}")
    public List<TicketDto> ticketActive(@PathVariable String id) {
        return ticketPool.findTicketsForVendor(id).stream().map(Ticket::toDto).toList();
    }

    @GetMapping("/vendor/ticket-removed/{:id}")
    public List<TicketDto> ticketRemoved(@PathVariable String id) {
        try {
            return JsonReader.readChunkedFiles(FilePaths.CUSTOMER.toString(), "customer", id, TicketDto.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Scheduled(fixedDelay = 2000)  // 10 seconds interval
    public void sendSimulationSummery() {
        List<CustomerSimulation> customThreadList = threadPool.findTargetClassThread(CustomerSimulation.class);
        List<VendorSimulation> vendorThreadList = threadPool.findTargetClassThread(VendorSimulation.class);
        Map<String, Integer> simulationSummery = new HashMap<>();
        simulationSummery.put("customerCount", customThreadList.size());
        simulationSummery.put("vendorCount", vendorThreadList.size());
        simulationSummery.put("activeCustomerCount", (int) customThreadList.stream().filter(ThreadExecutable::isRunning).count());
        simulationSummery.put("activeVendorCount", (int) vendorThreadList.stream().filter(ThreadExecutable::isRunning).count());
        messagingTemplate.convertAndSend("/summery/simulation", simulationSummery);
    }
}
