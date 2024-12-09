package org.backend.input;

import org.backend.enums.CommandLineOperationsTypes;
import org.backend.enums.LiveMonitorType;

public class CommandLineOperationInput extends UserInputAbstract {

    public static void displayOperations() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Command line operations ");
        System.out.println(CommandLineOperationsTypes.ADD_CUSTOMER);
        System.out.println(CommandLineOperationsTypes.ADD_VENDOR);
        System.out.println(CommandLineOperationsTypes.START_LIVE_EVENT_MONITOR);
        System.out.println(CommandLineOperationsTypes.START_LIVE_WEBSOCKET_EVENT_MONITOR);
        System.out.println(CommandLineOperationsTypes.EXIT);
        System.out.println("\n** Note - Enter only the number");
        System.out.println("   Press enter key to exit any sub operation");
        System.out.println("------------------------------------------------------------");
    }

    public static int getOperations() {
        return getValidIntegerInput("Enter operation", 1, 5);
    }

    public static int getQuantity(){
        return getValidIntegerInput("Enter quantity", 1, 20);
    }

    public static LiveMonitorType liveMonitorType(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Enter live operation type").append("\n1. ").append(LiveMonitorType.PERIOD_MONITOR.name().toLowerCase()).append("\n2. ").append(LiveMonitorType.INDIVIDUAL_MONITOR.name().toLowerCase()).append("\n").append(" ");
        int liveOperationType = getValidIntegerInput(stringBuilder.toString(), 1, 2);
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
