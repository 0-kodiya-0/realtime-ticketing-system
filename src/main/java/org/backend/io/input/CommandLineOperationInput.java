package org.backend.io.input;

import org.backend.enums.CommandLineOperationsTypes;
import org.backend.enums.CustomerTypes;
import org.backend.services.CustomerSimulation;
import org.backend.thread.ThreadExecutable;
import org.backend.services.VendorSimulation;

import java.util.HashMap;
import java.util.Map;

public class CommandLineOperationInput extends UserInputAbstract {

    public static void displayOperations() {
        System.out.println("------------------------------------------------------------");
        System.out.println("Command line operations ");
        System.out.println(CommandLineOperationsTypes.ADD_CUSTOMER);
        System.out.println(CommandLineOperationsTypes.REMOVE_CUSTOMER);
        System.out.println(CommandLineOperationsTypes.ADD_VENDOR);
        System.out.println(CommandLineOperationsTypes.REMOVE_VENDOR);
        System.out.println(CommandLineOperationsTypes.START_SIMULATION);
        System.out.println(CommandLineOperationsTypes.STOP_SIMULATION);
        System.out.println(CommandLineOperationsTypes.START_LIVE_EVENT_MONITOR);
        System.out.println(CommandLineOperationsTypes.START_LIVE_HTTP_SERVER_EVENT_MONITOR);
        System.out.println(CommandLineOperationsTypes.RESOURCE_STATISTICS);
        System.out.println(CommandLineOperationsTypes.EXIT);
        System.out.println("\n** Note - Enter only the number");
        System.out.println("   Press enter key to exit any sub operation");
        System.out.println("------------------------------------------------------------");
    }

    private static CustomerTypes getCustomerType() {
        int customerType = getValidIntegerInput("Enter customers type" + "\n1. " + CustomerTypes.VIP.name().toLowerCase() + "\n2. " + CustomerTypes.NOT_VIP.name().toLowerCase() + "\n" + " ", 1, 2);
        if (customerType == 1) {
            return CustomerTypes.VIP;
        } else {
            return CustomerTypes.NOT_VIP;
        }
    }

    private static int getQuantity() {
        return getValidIntegerInput("Enter quantity", 1, 20);
    }

    private static Class<? extends ThreadExecutable> getSimulationType(){
        int customerType = getValidIntegerInput("Enter simulation type" + "\n1. customer \n2. vendor \n3. both" + " ", 1, 3);
        if (customerType == 1) {
            return CustomerSimulation.class;
        } else if (customerType == 2) {
            return VendorSimulation.class;
        } else {
            return ThreadExecutable.class;
        }
    }

    public static Map<String, Object> getOperations() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        CommandLineOperationsTypes operation = CommandLineOperationsTypes.getCommand(getValidIntegerInput("Enter operation", 0, 9));
        switch (operation) {
            case ADD_CUSTOMER:
                stringObjectHashMap.put("quantity", getQuantity());
                stringObjectHashMap.put("customerType", getCustomerType());
                break;
            case REMOVE_CUSTOMER, ADD_VENDOR, REMOVE_VENDOR:
                stringObjectHashMap.put("quantity", getQuantity());
                break;
            case START_SIMULATION, STOP_SIMULATION:
                stringObjectHashMap.put("simulationType", getSimulationType());
                break;
        }
        stringObjectHashMap.put("operation", operation);
        return stringObjectHashMap;
    }

    public static void getEndContinuesOperation() {
        isEnterKeyPressed("");
    }
}
