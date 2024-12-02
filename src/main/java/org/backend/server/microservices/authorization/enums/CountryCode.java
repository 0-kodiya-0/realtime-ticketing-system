package org.backend.server.microservices.authorization.enums;

import lombok.Getter;

@Getter
public enum CountryCode {
    C_94("+94");

    private final String countryCode;

    CountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return countryCode;
    }
}
