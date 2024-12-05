package org.backend.server.microservices.authorization.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.annotations.IsRegexValid;

@Embeddable
@Data
public class Address {
    @NotNull
    @IsRegexValid(regexp = "^[a-z]+$")
    private String province;

    @NotNull
    @IsRegexValid(regexp = "^[a-z]+$")
    private String city;

    @NotNull
    @IsRegexValid(regexp = "^[a-z]+$")
    private String street;

    @NotNull
    @Column(unique = true)
    @IsRegexValid(regexp = "^[a-z\s]{5,30}+$")
    private String address;

    @NotNull
    @IsRegexValid(regexp = "^[0-9]{4,6}$")
    private String postalCode;
}
