package org.backend.cli;

import org.backend.cli.input.MainConfigurationInput;
import org.backend.dto.MainConfigurationDto;
import org.backend.cli.output.JsonWriter;
import org.backend.cli.services.ThreadEventPasser;
import org.backend.services.CustomerPool;
import org.backend.services.PurchasePool;
import org.backend.services.TicketPool;
import org.backend.services.VendorPool;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Cli {

    public static MainConfigurationDto config = new MainConfigurationDto();

    public static void main(String[] args) throws InterruptedException, IOException {

        MainConfigurationDto dto = MainConfigurationInput.getInput();
        System.out.println(dto);
        JsonWriter.writeToJsonPretty(dto, "./config.json");

        ThreadEventPasser customerThreadEventPasser = new ThreadEventPasser();
        ThreadEventPasser vendorThreadEventPasser = new ThreadEventPasser();
        ThreadEventPasser liveMonitorThreadEventPasser = new ThreadEventPasser();

        PurchasePool purchasePool = new PurchasePool();
        TicketPool ticketPool = new TicketPool(config.getTotalNumberOfTickets(), purchasePool);
        CustomerPool customerPool = new CustomerPool(20, purchasePool);
        VendorPool vendorPool = new VendorPool(20, ticketPool);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 100, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.CallerRunsPolicy());


//        LiveMonitoring liveMonitoring = new LiveMonitoring(LiveMonitorType.INDIVIDUAL_MONITOR, ticketPool, purchasePool, vendorPool, customerPool);
//
//        List<Thread> vendorThread = new ArrayList<Thread>();
//        List<Thread> customerThread = new ArrayList<Thread>();
//        Random randomNumberFinder = new Random();
//        for (int i = 0; i < 15; i++) {
//            vendorThread.add(new Thread(new VendorSimulation(TimeUnit.MINUTES.toMillis(1), 1000, 10, new Vendor(), vendorPool, ticketPool)));
//        }
//        for (Thread thread : vendorThread) {
//            thread.start();
//        }
//        for (int i = 0; i < 10; i++) {
//            customerThread.add(new Thread(new CustomerSimulation(TimeUnit.MINUTES.toMillis(1), randomNumberFinder.nextInt(500, 1000), 50, new Customer(), customerPool, ticketPool)));
//        }
//        for (Thread thread : customerThread) {
//            thread.start();
//        }
//        liveMonitoring.run();
//        for (Thread thread : vendorThread) {
//            thread.join();
//        }
//        for (Thread thread : customerThread) {
//            thread.join();
//        }
//        liveMonitoring.stop();
//        liveMonitoring.clearMem();
//        System.out.println("All vendorThread completed");
    }
}
