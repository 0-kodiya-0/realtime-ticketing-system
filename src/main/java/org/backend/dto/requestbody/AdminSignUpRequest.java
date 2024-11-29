package org.backend.dto.requestbody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.model.Admin;
import org.backend.model.Contact;
import org.backend.model.Credentials;

@Data
@NoArgsConstructor
public class AdminSignUpRequest {
    @NotNull
    @Valid
    private Admin admin;
    @NotNull
    @Valid
    private Credentials credentials;
    @NotNull
    @Valid
    private Contact contact;
}
