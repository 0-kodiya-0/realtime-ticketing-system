package org.backend.io.input;

import org.backend.enums.CommandLineOperationsTypes;
import org.backend.enums.CustomerTypes;
import org.backend.simulation.CustomerSimulation;
import org.backend.simulation.ThreadExecutable;
import org.backend.simulation.VendorSimulation;

import java.util.HashMap;
import java.util.Map;

/**
 * Define the logic where the command input are validated and asked from the user
 */
public class CommandLineOperationInput extends UserInputAbstract {

    /**
     * Displays the different operations and its associated number to the user
     */
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

    /**
     * Prompts the user to select the customer type (VIP or NOT_VIP).
     *
     * @return the selected customer type as a {@link CustomerTypes} enum value
     */
    private static CustomerTypes getCustomerType() {
        int customerType = getValidIntegerInput("Enter customers type" + "\n1. " + CustomerTypes.VIP.name().toLowerCase().replaceAll("_" , " ") + "\n2. " + CustomerTypes.NOT_VIP.name().toLowerCase().replaceAll("_", " ") + "\n" + " ", 1, 2);
        if (customerType == 1) {
            return CustomerTypes.VIP;
        } else {
            return CustomerTypes.NOT_VIP;
        }
    }

    private static int getQuantity() {
        return getValidIntegerInput("Enter quantity", 1, 20);
    }

    /**
     * Prompts the user to select the simulation type (customer, vendor, or both) to run or stop running simulations.
     *
     * @return the {@link Class} object representing the selected simulation type:
     *         <ul>
     *             <li>{@link CustomerSimulation} if the user selects customer simulation (option 1)</li>
     *             <li>{@link VendorSimulation} if the user selects vendor simulation (option 2)</li>
     *             <li>{@link ThreadExecutable} if the user selects both simulations (option 3)</li>
     *         </ul>
     */
    private static Class<? extends ThreadExecutable> getSimulationType(){
        int customerType = getValidIntegerInput("Enter simulation type" + "\n1. customer \n2. vendor \n3. both\n " + " ", 1, 3);
        if (customerType == 1) {
            return CustomerSimulation.class;
        } else if (customerType == 2) {
            return VendorSimulation.class;
        } else {
            return ThreadExecutable.class;
        }
    }

    /**
     * Prompts the user to select an operation and returns a map containing the operation details.
     *
     * @return a map with the following key-value pairs:
     *         <ul>
     *             <li>"operation" - The selected {@link CommandLineOperationsTypes} operation</li>
     *             <li>"quantity" - Quantity (if applicable for the selected operation)</li>
     *             <li>"customerType" - The {@link CustomerTypes} enum value (if applicable for the selected operation)</li>
     *             <li>"simulationType" - The simulation type as a {@link Class} object (if applicable for the selected operation)</li>
     *         </ul>
     */
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

    /**
     * Waits for the user to press the Enter key to continue the operation.
     * This method is used to end running programs such as spring boot server or live monitoring.
     */
    public static void getEndContinuesOperation() {
        isEnterKeyPressed("");
    }
}
