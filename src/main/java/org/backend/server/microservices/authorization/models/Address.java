package org.backend.server.microservices.authorization.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Embeddable
@Data
public class Address {
    @NotNull
    private String province;

    @NotNull
    private String city;

    @NotNull
    private String street;

    @NotNull
    @Column(unique = true)
    private String address;

    @NotNull
    private String postalCode;
}
