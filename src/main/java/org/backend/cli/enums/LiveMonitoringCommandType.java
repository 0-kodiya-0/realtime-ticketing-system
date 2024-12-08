package org.backend.cli.enums;

import org.backend.services.CustomerPool;
import org.backend.services.PurchasePool;
import org.backend.services.TicketPool;
import org.backend.services.VendorPool;

public enum LiveMonitoringCommandType {
    CUSTOMER("customer", CustomerPool.class),
    VENDOR("vendor", VendorPool.class),
    TICKET("ticket", TicketPool.class),
    PURCHASE("purchase", PurchasePool.class);

    private final String command;
    private final Class<?> serviceClass;

    LiveMonitoringCommandType(String command, Class<?> serviceClass) {
        this.command = command;
        this.serviceClass = serviceClass;
    }

    public static Class<?> getServiceClass(String command) {
        for (LiveMonitoringCommandType cmd : values()) {
            if (cmd.command.equals(command)) {
                return cmd.serviceClass;
            }
        }
        return null;
    }
}
