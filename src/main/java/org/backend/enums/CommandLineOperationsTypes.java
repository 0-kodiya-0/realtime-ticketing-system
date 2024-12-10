package org.backend.enums;

public enum CommandLineOperationsTypes {
    ADD_CUSTOMER(1), REMOVE_CUSTOMER(2), ADD_VENDOR(3), REMOVE_VENDOR(4), START_SIMULATION(5), STOP_SIMULATION(6), START_LIVE_EVENT_MONITOR(7), START_LIVE_HTTP_SERVER_EVENT_MONITOR(8), RESOURCE_STATISTICS(9), EXIT(0);

    private final int integer;

    CommandLineOperationsTypes(int i) {
        this.integer = i;
    }

    public static CommandLineOperationsTypes getCommand(int eventInteger) {
        for (CommandLineOperationsTypes command : CommandLineOperationsTypes.values()) {
            if (command.getInteger() == eventInteger) {
                return command;
            }
        }
        throw new IllegalArgumentException("No command found with value " + eventInteger);
    }

    public int getInteger() {
        return integer;
    }

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
