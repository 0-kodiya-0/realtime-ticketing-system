package org.backend.server.microservices.authorization.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.annotations.IsRegexValid;
import org.backend.server.microservices.authorization.enums.CountryCode;

@Embeddable
@Data
public class PhoneNumber {
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode countryCode;

    @NotNull
    @Column(nullable = false, unique = true)
    @IsRegexValid(regexp = "^[0-9]{9,10}$")
    private String phoneNumber;
}
