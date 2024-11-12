package org.example.opp_cw.dto.requestbody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.model.Admin;
import org.example.opp_cw.model.Contact;
import org.example.opp_cw.model.Credentials;

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
