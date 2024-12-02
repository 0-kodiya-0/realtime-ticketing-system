package org.backend.server.microservices.authorization.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.annotations.IsRegexValid;

@Embeddable
@Data
public class Contact {

    @NotNull
    @Column(nullable = false, unique = true)
    @IsRegexValid(regexp = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$")
    private String email;

    @Column(name = "isEmailVerified", nullable = false)
    private boolean emailVerified = false;

    @NotNull
    @Valid
    @Embedded
    @Column(nullable = false)
    private PhoneNumber phoneNumber;

    @Column(name = "isPhoneNumberVerified", nullable = false)
    private boolean phoneNumberVerified = false;

}
