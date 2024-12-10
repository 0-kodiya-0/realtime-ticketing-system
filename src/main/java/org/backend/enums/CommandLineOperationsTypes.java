package org.backend.enums;

public enum CommandLineOperationsTypes {
    ADD_CUSTOMER(1), ADD_VENDOR(2), START_SIMULATION(3), STOP_SIMULATION(4), START_LIVE_EVENT_MONITOR(5), START_LIVE_WEBSOCKET_EVENT_MONITOR(6), EXIT(7);

    int integer;

    CommandLineOperationsTypes(int i) {
        this.integer = i;
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
