package org.backend;

import org.backend.dto.MainConfigurationDto;
import org.backend.enums.CommandLineOperationsTypes;
import org.backend.enums.CustomerTypes;
import org.backend.io.file.JsonWriter;
import org.backend.io.input.CommandLineOperationInput;
import org.backend.io.input.MainConfigurationInput;
import org.backend.pools.PurchasePool;
import org.backend.pools.ThreadPool;
import org.backend.pools.TicketPool;
import org.backend.server.SpringBootServer;
import org.backend.services.LiveMonitoring;
import org.backend.simulation.CustomThread;
import org.backend.simulation.CustomerSimulation;
import org.backend.simulation.ThreadExecutable;
import org.backend.simulation.VendorSimulation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Cli {

    private static final ThreadPool threadPool = new ThreadPool(100);
    private static MainConfigurationDto mainConfiguration;
    private static PurchasePool purchasePool;
    private static TicketPool ticketPool;

    /**
     * Adds a new customer to the system based on provided parameters by the user.
     * Takes a map of main configurations properties that are input by the user.
     *
     * @param operations Map containing customer main configuration parameters
     */
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

    /**
     * Removes specified number of customers from the system and their associated threads.
     * Processes customer removal based on quantity specified in operations map and
     * terminates their execution threads from the thread pool.
     *
     * @param operations Map containing removal parameters including 'quantity' of customers
     */
    public static void removeCustomer(Map<String, Object> operations) {
        int customerRemoveQuantity = (Integer) operations.get("quantity");
        try {
            int customerRemovedThreads = threadPool.removeRunnable(CustomerSimulation.class, customerRemoveQuantity);
            System.out.println("Removed " + customerRemovedThreads + " customer threads");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds specified number of vendors to the system based on operation parameters.
     * Processes vendor addition requests by creating new vendor instances and their associated threads in the thread pool.
     *
     * @param operations Map containing vendor configuration parameters including 'quantity'
     */
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

    /**
     * Removes specified number of vendors from the system and their associated resources.
     * Processes vendor removal based on quantity parameter, cleaning up vendor threads and associated tickets from the pool.
     * @param operations Map containing removal parameters including 'quantity' of vendors
     */
    public static void removeVendor(Map<String, Object> operations) {
        int vendorRemoveQuantity = (Integer) operations.get("quantity");
        try {
            int vendorRemovedThreads = threadPool.removeRunnable(VendorSimulation.class, vendorRemoveQuantity);
            System.out.println("Removed " + vendorRemovedThreads + " vendor threads");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initiates the system simulation based on provided configuration parameters.
     * Starts vendor and customer simulations, initializes system resources, and
     * begins ticket operations according to the operation {simulationType}.
     * @param operations Map containing simulation parameters and configuration settings
     */
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

    /**
     * Initializes and starts a live monitoring thread for tracking system events.
     * Creates a custom thread for monitoring ticket and purchase pool activities
     * in real-time using a dedicated LiveMonitoring instance.
     * @param operations Map containing monitor configuration parameters
     */
    public static void startLiveEventMonitor(Map<String, Object> operations) {
        CustomThread thread = new CustomThread(new LiveMonitoring(ticketPool, purchasePool));
        thread.start();
        CommandLineOperationInput.getEndContinuesOperation();
        thread.interrupt();
        System.out.println("Live monitor thread ended");
    }

    /**
     * Initiates a live HTTP server for monitoring system events and pool states.
     * Starts a Spring Boot server instance to expose monitoring endpoints, waits for
     * user termination signal, then gracefully shuts down the server.
     *
     * @param operations Map containing server configuration parameters
     */
    public static void startLiveHttpServerEventMonitor(Map<String, Object> operations) {
        SpringBootServer springBootServer = new SpringBootServer();
        springBootServer.run(ticketPool, purchasePool, threadPool, mainConfiguration);
        CommandLineOperationInput.getEndContinuesOperation();
        springBootServer.stop();
        System.out.println("Server monitor thread ended");
    }

    /**
     * Displays current system resource statistics to the console.
     * Prints information about resource utilization, pool states, and other relevant system metrics for monitoring purposes.
     */
    public static void resourceStatistics() {
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

        //Save the created configuration
        JsonWriter.writeToJsonPretty(mainConfiguration, "./config.json");

        purchasePool = new PurchasePool(Integer.MAX_VALUE); // No limit for the purchase pool. Can run until out of memory
        ticketPool = new TicketPool(mainConfiguration.getMaximumTicketCapacity(), purchasePool);

        /*
         * Main command loop that processes user operations from the command line.
         * Continuously displays available operations, reads user input, and processes
         * the commands until thread interruption. Handles operation parsing errors
         * gracefully with user feedback.
         * @throws InterruptedException if the thread is interrupted during execution
         */
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
                    resourceStatistics();
                    break;
                case EXIT:
                    // Stop any running threads
                    threadPool.stopRunningThreads(CustomerSimulation.class);
                    threadPool.stopRunningThreads(VendorSimulation.class);

                    // Removes the customers and vendors from the thread pool
                    threadPool.removeRunnable(CustomerSimulation.class, threadPool.getPoolUsedCapacity());
                    threadPool.removeRunnable(VendorSimulation.class, threadPool.getPoolUsedCapacity());
                    System.out.println("Executing stopped for main thread");
                    System.exit(0);
                    break;
            }
        }
    }
}
