package org.backend.enums;

public enum FilePaths {
    CUSTOMER("./customer-data"), VENDOR("./vendor-data"), CONFIG("./config.json");

    final String directory;

    FilePaths(String directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return directory;
    }
}
