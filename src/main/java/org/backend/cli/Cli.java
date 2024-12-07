package org.backend.cli;

import org.backend.cli.input.LiveMonitoring;
import org.backend.cli.input.MainConfiguration;
import org.backend.cli.services.*;

public class Cli {

//    public static MainConfiguration config = new MainConfiguration();
//    public static LiveMonitoring liveMonitoring = new LiveMonitoring();



    public static void main(String[] args) throws InterruptedException {
        PurchasePool purchasePool = new PurchasePool();
        TicketPool ticketPool = new TicketPool(purchasePool, 100, 10);
        CustomerPool customerPool = new CustomerPool(purchasePool, 20);
        VendorPool vendorPool = new VendorPool(ticketPool, 20);
        CustomerSimulation customerSimulation = new CustomerSimulation(customerPool, ticketPool, 1);
        VendorSimulation vendorSimulation = new VendorSimulation(vendorPool, ticketPool, 10, 1000);

        Thread thread = new Thread(customerSimulation);
        Thread thread2 = new Thread(vendorSimulation);
        thread.start();
        Thread.sleep(1000);
        thread2.start();
        thread.join();

        System.out.println(ticketPool.getInUseTickets());
        System.out.println(customerPool.getInUseCustomers());
        System.out.println(vendorPool.getInUseVendor());
        System.out.println(purchasePool.getInUsePurchaseHistory());
    }
}
