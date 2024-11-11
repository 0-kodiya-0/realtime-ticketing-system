package org.example.opp_cw.dto.requestbody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.model.Contact;
import org.example.opp_cw.model.Credentials;
import org.example.opp_cw.model.Customer;

@Data
@NoArgsConstructor
public class SignUpRequest {
    @NotNull
    @Valid
    private Customer customer;
    @NotNull
    @Valid
    private Credentials credentials;
    @NotNull
    @Valid
    private Contact contact;
}
