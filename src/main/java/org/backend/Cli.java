package org.backend;

import org.backend.dto.MainConfigurationDto;
import org.backend.enums.CommandLineOperationsTypes;
import org.backend.enums.CustomerTypes;
import org.backend.io.input.CommandLineOperationInput;
import org.backend.io.input.MainConfigurationInput;
import org.backend.io.file.JsonWriter;
import org.backend.pools.PurchasePool;
import org.backend.pools.ThreadPool;
import org.backend.pools.TicketPool;
import org.backend.server.SpringBootServer;
import org.backend.services.CustomerSimulation;
import org.backend.services.LiveMonitoring;
import org.backend.services.VendorSimulation;
import org.backend.thread.CustomThread;
import org.backend.thread.ThreadExecutable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Cli {

    private static final ThreadPool threadPool = new ThreadPool(100);
    private static MainConfigurationDto mainConfiguration;
    private static PurchasePool purchasePool;
    private static TicketPool ticketPool;

    public static void addCustomer(Map<String, Object> operations) {
        int customerAddQuantity = (Integer) operations.get("quantity");
        CustomerTypes customerType = (CustomerTypes) operations.get("customerType");
        try {
            int customerAddedRunnableList = threadPool.addRunnable(() -> {
                return new CustomerSimulation(mainConfiguration.getTicketRetrievalRate(), customerType, ticketPool, purchasePool);
            }, customerAddQuantity);
            System.out.println("Added " + customerAddedRunnableList + " customer threads");
            if (customerAddedRunnableList < customerAddQuantity) {
                System.out.println("Not added " + (customerAddQuantity - customerAddedRunnableList) + " customer threads");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeCustomer(Map<String, Object> operations) {
        int customerRemoveQuantity = (Integer) operations.get("quantity");
        try {
            int customerRemovedThreads = threadPool.removeRunnable(CustomerSimulation.class, customerRemoveQuantity);
            System.out.println("Removed " + customerRemovedThreads + " customer threads");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addVendor(Map<String, Object> operations) {
        int vendorAddQuantity = (Integer) operations.get("quantity");
        try {
            int vendorAddedThreads = threadPool.addRunnable(() -> {
                return new VendorSimulation(mainConfiguration.getTicketReleaseRate(), ticketPool);
            }, vendorAddQuantity);
            System.out.println("Added " + vendorAddedThreads + " vendor threads");
            if (vendorAddedThreads < vendorAddQuantity) {
                System.out.println("Not added " + (vendorAddQuantity - vendorAddedThreads) + " vendor threads");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeVendor(Map<String, Object> operations) {
        int vendorRemoveQuantity = (Integer) operations.get("quantity");
        try {
            int vendorRemovedThreads = threadPool.removeRunnable(VendorSimulation.class, vendorRemoveQuantity);
            System.out.println("Removed " + vendorRemovedThreads + " vendor threads");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void startSimulation(Map<String, Object> operations) {
        int startedRunningThreads;
        if (operations.get("simulationType").equals(VendorSimulation.class)) {
            startedRunningThreads = threadPool.startNotRunningThreads(VendorSimulation.class);
        } else if (operations.get("simulationType").equals(CustomerSimulation.class)) {
            startedRunningThreads = threadPool.startNotRunningThreads(CustomerSimulation.class);
        } else {
            startedRunningThreads = threadPool.startNotRunningThreads(CustomerSimulation.class);
            startedRunningThreads += threadPool.startNotRunningThreads(VendorSimulation.class);
        }
        System.out.println("Started " + startedRunningThreads + " running threads");
    }

    public static void stopSimulation(Map<String, Object> operations) {
        int stoppedRunningThreads;
        if (operations.get("simulationType").equals(VendorSimulation.class)) {
            stoppedRunningThreads = threadPool.stopRunningThreads(VendorSimulation.class);
        } else if (operations.get("simulationType").equals(CustomerSimulation.class)) {
            stoppedRunningThreads = threadPool.stopRunningThreads(CustomerSimulation.class);
        } else {
            stoppedRunningThreads = threadPool.stopRunningThreads(CustomerSimulation.class);
            stoppedRunningThreads += threadPool.stopRunningThreads(VendorSimulation.class);
        }
        System.out.println("Stopped " + stoppedRunningThreads + " running threads");
    }

    public static void startLiveEventMonitor(Map<String, Object> operations) {
        CustomThread thread = new CustomThread(new LiveMonitoring(ticketPool, purchasePool));
        thread.start();
        CommandLineOperationInput.getEndContinuesOperation();
        thread.interrupt();
        System.out.println("Live monitor thread ended");
    }

    public static void startLiveHttpServerEventMonitor(Map<String, Object> operations) {
        SpringBootServer springBootServer = new SpringBootServer();
        springBootServer.run(ticketPool, purchasePool, threadPool);
        CommandLineOperationInput.getEndContinuesOperation();
        springBootServer.stop();
        System.out.println("Server monitor thread ended");
    }

    public static void resourceStatistics(Map<String, Object> operations) {
        System.out.println("Resource statistics");
        System.out.println("Total used threads : " + threadPool.getInUseObjects().size());
        System.out.println("Total interrupted threads : " + threadPool.findInterruptedThreads().size());
        System.out.println("Total running threads : " + threadPool.findRunningThreads().size());
        System.out.println("Total purchase pool count : " + purchasePool.getInUseObjects().size());
        System.out.println("Total ticket pool count : " + ticketPool.getInUseObjects().size());
        List<CustomerSimulation> customThreadList = threadPool.findTargetClassThread(CustomerSimulation.class);
        List<VendorSimulation> vendorThreadList = threadPool.findTargetClassThread(VendorSimulation.class);
        System.out.println("Total customer count : " + customThreadList.size());
        System.out.println("Total vendor count : " + vendorThreadList.size());
        System.out.println("Total active customer count : " + customThreadList.stream().filter(ThreadExecutable::isRunning).count());
        System.out.println("Total active vendor count : " + vendorThreadList.stream().filter(ThreadExecutable::isRunning).count());
    }

    public static void main(String[] args) throws IOException {
        mainConfiguration = MainConfigurationInput.getInput();
        JsonWriter.writeToJsonPretty(mainConfiguration, "./config.json");

        purchasePool = new PurchasePool(Integer.MAX_VALUE);
        ticketPool = new TicketPool(mainConfiguration.getMaximumTicketCapacity(), purchasePool);

        while (!CustomThread.currentThread().isInterrupted()) {
            CommandLineOperationInput.displayOperations();
            Map<String, Object> operations;
            try {
                operations = CommandLineOperationInput.getOperations();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            switch ((CommandLineOperationsTypes) operations.get("operation")) {
                case ADD_CUSTOMER:
                    addCustomer(operations);
                    break;
                case REMOVE_CUSTOMER:
                    removeCustomer(operations);
                    break;
                case ADD_VENDOR:
                    addVendor(operations);
                    break;
                case REMOVE_VENDOR:
                    removeVendor(operations);
                    break;
                case START_SIMULATION:
                    startSimulation(operations);
                    break;
                case STOP_SIMULATION:
                    stopSimulation(operations);
                    break;
                case START_LIVE_EVENT_MONITOR:
                    startLiveEventMonitor(operations);
                    break;
                case START_LIVE_HTTP_SERVER_EVENT_MONITOR:
                    startLiveHttpServerEventMonitor(operations);
                    break;
                case RESOURCE_STATISTICS:
                    resourceStatistics(operations);
                    break;
                case EXIT:
                    threadPool.stopRunningThreads(CustomerSimulation.class);
                    threadPool.stopRunningThreads(VendorSimulation.class);
                    System.out.println("Executing stopped for main thread");
                    System.exit(0);
                    break;
            }
        }
    }
}
