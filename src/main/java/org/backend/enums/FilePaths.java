package org.backend.enums;

/**
 * Enum representing the various files paths the programme use
 *
 * CUSTOMER: Customer data saving directory which the customer save its simulation progress
 * VENDOR: Vendor data saving directory which the vendor save its simulation progress
 * CONFIG: Main configuration file directory which saves the configuration data of the programme for latter use
 */
public enum FilePaths {
    CUSTOMER("./customer-data"), VENDOR("./vendor-data"), CONFIG("./config.json");

    final String directory;

    FilePaths(String directory) {
        this.directory = directory;
    }

    /**
     * @return Return the associated file path for the enum
     */
    @Override
    public String toString() {
        return directory;
    }
}
