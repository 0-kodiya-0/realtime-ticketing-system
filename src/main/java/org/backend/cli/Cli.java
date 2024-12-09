package org.backend.cli;

import org.backend.cli.enums.LiveMonitorType;
import org.backend.cli.input.MainConfigurationInput;
import org.backend.cli.output.JsonWriter;
import org.backend.cli.services.*;
import org.backend.dto.MainConfigurationDto;
import org.backend.enums.EventTypes;
import org.backend.model.Customer;
import org.backend.model.Vendor;
import org.backend.services.PurchasePool;
import org.backend.services.TicketPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Cli {

    private static final ThreadEventPasser customerThreadEventPasser = new ThreadEventPasser();
    private static final ThreadEventPasser vendorThreadEventPasser = new ThreadEventPasser();
    private static final ThreadEventPasser liveMonitorThreadEventPasser = new ThreadEventPasser();

    public static void main(String[] args) throws IOException {

        MainConfigurationDto mainConfiguration = MainConfigurationInput.getInput();
        JsonWriter.writeToJsonPretty(mainConfiguration, "./config.json");

        System.out.println(mainConfiguration);

        PurchasePool purchasePool = new PurchasePool();
        TicketPool ticketPool = new TicketPool(mainConfiguration.getMaximumTicketCapacity(), purchasePool);
        LiveMonitoring liveMonitoring = new LiveMonitoring(LiveMonitorType.PERIOD_MONITOR, liveMonitorThreadEventPasser, ticketPool, purchasePool);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 100, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(50), new ThreadPoolExecutor.CallerRunsPolicy());

        List<SimulationAbstract> simulations = new ArrayList<>();

        executor.submit(liveMonitoring);

        executor.submit(() ->{
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the number of simulations you wish to run: ");
            System.out.println(scanner.nextLine());
        });

        for (int i = 0; i < mainConfiguration.getTotalNumberOfVendors(); i++) {
            VendorSimulation vendorSimulation = new VendorSimulation(mainConfiguration.getTicketReleaseRate(), vendorThreadEventPasser, new Vendor(), ticketPool);
            simulations.add((SimulationAbstract) vendorSimulation);
            executor.execute(vendorSimulation);
        }

        for (int i = 0; i < mainConfiguration.getTotalNumberOfCustomers(); i++) {
            CustomerSimulation customerSimulation = new CustomerSimulation(mainConfiguration.getTicketRetrievalRate(), customerThreadEventPasser, new Customer(), ticketPool, purchasePool);
            simulations.add((SimulationAbstract) customerSimulation);
            executor.execute(customerSimulation);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                customerThreadEventPasser.sendEvent(EventTypes.REMOVED_THREAD);
                vendorThreadEventPasser.sendEvent(EventTypes.REMOVED_THREAD);
                liveMonitorThreadEventPasser.sendEvent(EventTypes.REMOVED_THREAD);
                for (SimulationAbstract simulation : simulations) {
                    try {
                        simulation.stop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("\nShutdown signal received...");
            }
        });
    }
}
