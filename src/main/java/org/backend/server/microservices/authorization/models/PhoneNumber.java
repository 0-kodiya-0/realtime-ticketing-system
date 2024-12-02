package org.backend.server.microservices.authorization.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.microservices.authorization.enums.CountryCode;

@Embeddable
@Data
public class PhoneNumber {
    @NotNull
    @Column(nullable = false)
    private CountryCode countryCode;

    @NotNull
    @Column(nullable = false, unique = true, length = 10)
    private String phoneNumber;
}
