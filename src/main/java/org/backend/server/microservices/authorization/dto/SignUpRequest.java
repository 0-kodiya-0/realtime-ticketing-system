package org.backend.server.microservices.authorization.dto;

import jakarta.validation.Valid;
import lombok.Data;
import org.backend.server.microservices.authorization.models.Contact;
import org.backend.server.microservices.authorization.models.Credentials;
import org.backend.server.microservices.authorization.models.Person;

@Data
public class SignUpRequest {
    @Valid
    private Person person;
    @Valid
    private Credentials credentials;
    @Valid
    private Contact contact;
}
