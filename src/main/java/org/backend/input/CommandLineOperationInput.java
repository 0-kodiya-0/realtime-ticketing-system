package org.backend.input;

import org.backend.enums.CommandLineOperationsTypes;
import org.backend.enums.CustomerTypes;
import org.backend.enums.LiveMonitorType;

public class CommandLineOperationInput extends UserInputAbstract {

    public static void displayOperations() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Command line operations ");
        System.out.println(CommandLineOperationsTypes.ADD_CUSTOMER);
        System.out.println(CommandLineOperationsTypes.ADD_VENDOR);
        System.out.println(CommandLineOperationsTypes.START_SIMULATION);
        System.out.println(CommandLineOperationsTypes.STOP_SIMULATION);
        System.out.println(CommandLineOperationsTypes.START_LIVE_EVENT_MONITOR);
        System.out.println(CommandLineOperationsTypes.START_LIVE_WEBSOCKET_EVENT_MONITOR);
        System.out.println(CommandLineOperationsTypes.EXIT);
        System.out.println("\n** Note - Enter only the number");
        System.out.println("   Press enter key to exit any sub operation");
        System.out.println("------------------------------------------------------------");
    }

    public static int getOperations() {
        return getValidIntegerInput("Enter operation", 1, 7);
    }

    public static CustomerTypes getCustomerType() {
        int customerType = getValidIntegerInput("Enter customers type" + "\n1. " + CustomerTypes.VIP.name().toLowerCase() + "\n2. " + CustomerTypes.NOT_VIP.name().toLowerCase() + "\n" + " ", 1, 2);
        if (customerType == 1) {
            return CustomerTypes.VIP;
        } else {
            return CustomerTypes.NOT_VIP;
        }
    }

    public static int getQuantity() {
        return getValidIntegerInput("Enter quantity", 1, 20);
    }

    public static LiveMonitorType liveMonitorType() {
        int liveOperationType = getValidIntegerInput("Enter live operation type" + "\n1. " + LiveMonitorType.PERIOD_MONITOR.name().toLowerCase() + "\n2. " + LiveMonitorType.INDIVIDUAL_MONITOR.name().toLowerCase() + "\n" + " ", 1, 2);
        if (liveOperationType == 1) {
            return LiveMonitorType.PERIOD_MONITOR;
        } else {
            return LiveMonitorType.INDIVIDUAL_MONITOR;
        }
    }

    public static void isEnd() {
        isEnterKeyPressed("");
    }
}
