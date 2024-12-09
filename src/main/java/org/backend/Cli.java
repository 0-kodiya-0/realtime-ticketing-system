package org.backend;

import org.backend.dto.MainConfigurationDto;
import org.backend.enums.CommandLineOperationsTypes;
import org.backend.enums.EventTypes;
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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

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

        while (true) {
            CommandLineOperationInput.displayOperations();
            int operations = CommandLineOperationInput.getOperations();
            if (CommandLineOperationsTypes.ADD_CUSTOMER.getInteger() == operations) {
                int customerQuantity = CommandLineOperationInput.getQuantity();
                threadPool.execute(new CustomerSimulation(mainConfiguration.getTicketRetrievalRate(), customerThreadEventPasser, new Customer(), ticketPool, purchasePool), customerQuantity);
            } else if (CommandLineOperationsTypes.ADD_VENDOR.getInteger() == operations) {
                int vendorQuantity = CommandLineOperationInput.getQuantity();
                threadPool.execute(new VendorSimulation(mainConfiguration.getTicketReleaseRate(), vendorThreadEventPasser, new Vendor(), ticketPool), vendorQuantity);
            } else if (CommandLineOperationsTypes.START_LIVE_EVENT_MONITOR.getInteger() == operations) {
                liveMonitorThreadEventPasser.sendEvent(EventTypes.START_THREAD);
                LiveMonitorType liveMonitorType = CommandLineOperationInput.liveMonitorType();
                threadPool.execute(new LiveMonitoring(liveMonitorType, liveMonitorThreadEventPasser, ticketPool, purchasePool));
                CommandLineOperationInput.isEnd();
                liveMonitorThreadEventPasser.sendEvent(EventTypes.REMOVED_THREAD);
            } else if (CommandLineOperationsTypes.START_LIVE_WEBSOCKET_EVENT_MONITOR.getInteger() == operations) {
                ConfigurableApplicationContext run = SpringApplication.run(Cli.class, args);
                CommandLineOperationInput.isEnd();
                run.stop();
            } else if (CommandLineOperationsTypes.EXIT.getInteger() == operations) {
                threadPool.shutdownAll();
                break;
            } else {
                System.out.println("\n Unknown operation: " + operations);
            }
        }
    }
}
