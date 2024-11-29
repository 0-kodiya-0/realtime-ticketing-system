package org.backend.dto.requestbody;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.model.Contact;
import org.backend.model.Credentials;
import org.backend.model.Customer;

@Data
@NoArgsConstructor
public class CustomerSignUpRequest {
    @Valid
    private Customer customer;
    @Valid
    private Credentials credentials;
    @Valid
    private Contact contact;
}
