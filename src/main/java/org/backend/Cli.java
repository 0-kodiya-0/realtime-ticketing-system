package org.backend;

import org.backend.dto.MainConfigurationDto;
import org.backend.enums.CommandLineOperationsTypes;
import org.backend.enums.CustomerTypes;
import org.backend.enums.LiveMonitorType;
import org.backend.input.CommandLineOperationInput;
import org.backend.input.MainConfigurationInput;
import org.backend.model.Customer;
import org.backend.model.Vendor;
import org.backend.output.JsonWriter;
import org.backend.pools.PurchasePool;
import org.backend.pools.ThreadPool;
import org.backend.pools.TicketPool;
import org.backend.services.CustomerSimulation;
import org.backend.services.LiveMonitoring;
import org.backend.services.ThreadEventPasser;
import org.backend.services.VendorSimulation;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Cli {

    private static final ThreadEventPasser customerThreadEventPasser = new ThreadEventPasser();
    private static final ThreadEventPasser vendorThreadEventPasser = new ThreadEventPasser();
    private static final ThreadEventPasser liveMonitorThreadEventPasser = new ThreadEventPasser();

    public static void main(String[] args) throws IOException {
        ThreadPool threadPool = new ThreadPool(100);

        MainConfigurationDto mainConfiguration = MainConfigurationInput.getInput();
        JsonWriter.writeToJsonPretty(mainConfiguration, "./config.json");

        PurchasePool purchasePool = new PurchasePool(Integer.MAX_VALUE);
        TicketPool ticketPool = new TicketPool(mainConfiguration.getMaximumTicketCapacity(), purchasePool);

        while (!Thread.currentThread().isInterrupted()) {
            CommandLineOperationInput.displayOperations();
            int operations = CommandLineOperationInput.getOperations();
            if (CommandLineOperationsTypes.ADD_CUSTOMER.getInteger() == operations) {

                int customerQuantity = CommandLineOperationInput.getQuantity();
                CustomerTypes customerType = CommandLineOperationInput.getCustomerType();
                Customer customer = new Customer();
                customer.setType(customerType);
                int threadPriority = Thread.MIN_PRIORITY;
                if (customerType == CustomerTypes.VIP) {
                    threadPriority = Thread.MAX_PRIORITY;
                }
                List<Thread> threads = threadPool.add(new CustomerSimulation(mainConfiguration.getTicketRetrievalRate(), customerThreadEventPasser, customer, ticketPool, purchasePool), customerQuantity, threadPriority);

                for (int i = 0; i < threads.size(); i++) {
                    System.out.println("Thread added " + threads.get(i).getName());
                }

            } else if (CommandLineOperationsTypes.ADD_VENDOR.getInteger() == operations) {

                int vendorQuantity = CommandLineOperationInput.getQuantity();
                List<Thread> threads = threadPool.add(new VendorSimulation(mainConfiguration.getTicketReleaseRate(), vendorThreadEventPasser, new Vendor(), ticketPool), vendorQuantity, Thread.NORM_PRIORITY);

                for (int i = 0; i < threads.size(); i++) {
                    System.out.println("Thread added " + threads.get(i).getName());
                }

            } else if (CommandLineOperationsTypes.START_SIMULATION.getInteger() == operations) {

                List<Thread> threads = threadPool.executeAll();
                for (int i = 0; i < threads.size(); i++) {
                    System.out.println("Executing started for thread " + threads.get(i).getName());
                }

            } else if (CommandLineOperationsTypes.STOP_SIMULATION.getInteger() == operations) {

                List<Thread> threads = threadPool.shutdownAll();
                for (int i = 0; i < threads.size(); i++) {
                    System.out.println("Executing stopped for thread " + threads.get(i).getName());
                }

            } else if (CommandLineOperationsTypes.START_LIVE_EVENT_MONITOR.getInteger() == operations) {

                LiveMonitorType liveMonitorType = CommandLineOperationInput.liveMonitorType();
                Thread thread = threadPool.addAndExecute(new LiveMonitoring(liveMonitorType, liveMonitorThreadEventPasser, ticketPool, purchasePool), Thread.NORM_PRIORITY);
                CommandLineOperationInput.isEnd();
                threadPool.shutdown(thread);
                System.out.println("Executing stopped for thread " + thread.getName());

            } else if (CommandLineOperationsTypes.START_LIVE_WEBSOCKET_EVENT_MONITOR.getInteger() == operations) {

//                ConfigurableApplicationContext run = SpringApplication.run(Cli.class, args);
//                CommandLineOperationInput.isEnd();
//                run.stop();

            } else if (CommandLineOperationsTypes.EXIT.getInteger() == operations) {

                threadPool.shutdownAll();
                System.out.println("Executing stopped for main thread");
                System.exit(0);
                return;

            } else {
                System.out.println("\n Unknown operation: " + operations);
            }
        }
    }
}
