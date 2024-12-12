package org.backend.enums;

import lombok.Getter;

/**
 * Enum representing the various input command the use can enter and the command number associated for it.
 *
 * ADD_CUSTOMER: Add a customer to a list to be executed.
 * REMOVE_CUSTOMER: Removes the customer which can be active or not active permanently
 * ADD_VENDOR: Add a vendor to a list to be executed.
 * REMOVE_VENDOR: Removes the vendor which can be active or not active permanently
 * START_SIMULATION: Create and starts new threads for the vendor, customer or both that is added
 * STOP_SIMULATION: Stops the created threads for the vendor, customer or both that is added and removes only the created thread
 * START_LIVE_EVENT_MONITOR: Start the live cli monitor that displays summery of the simulation mapping in the background
 * START_LIVE_HTTP_SERVER_EVENT_MONITOR: Start the live http server with spring boot to streamline background operation through http
 * RESOURCE_STATISTICS: Display the resource details such as the thread count, ticket pool count
 * EXIT: Exits the program and terminate any running thread
 */
@Getter
public enum CommandLineOperationsTypes {
    ADD_CUSTOMER(1), REMOVE_CUSTOMER(2), ADD_VENDOR(3), REMOVE_VENDOR(4), START_SIMULATION(5), STOP_SIMULATION(6), START_LIVE_EVENT_MONITOR(7), START_LIVE_HTTP_SERVER_EVENT_MONITOR(8), RESOURCE_STATISTICS(9), EXIT(0);

    /**
     * Command associated number. This number static and pre define in the programme
     */
    private final int integer;

    CommandLineOperationsTypes(int i) {
        this.integer = i;
    }

    /**
     * Retrieves the CommandLineOperationsTypes enum constant associated with the specified integer value.
     *
     * @param eventInteger The integer value representing the command
     * @return the CommandLineOperationsTypes enum constant associated with the specified integer value
     * @throws IllegalArgumentException If no command is found with the specified integer value
     */
    public static CommandLineOperationsTypes getCommand(int eventInteger) {
        for (CommandLineOperationsTypes command : CommandLineOperationsTypes.values()) {
            if (command.getInteger() == eventInteger) {
                return command;
            }
        }
        throw new IllegalArgumentException("No command found with value " + eventInteger);
    }

    /**
     * Returns a string representation of the command with its associated number and name.
     *
     * The string representation consists of the command's integer value, followed by a dot and space,
     * and then the command name in lowercase with underscores replaced by spaces.
     *
     * @return String representation of the command
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(integer).append(". ");
        for (String value : this.name().toLowerCase().split("_")) {
            builder.append(value).append(" ");
        }
        return builder.toString();
    }
}
