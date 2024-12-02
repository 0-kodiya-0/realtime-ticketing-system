package org.backend.server.microservices.authorization.dto;

import lombok.Data;
import org.backend.server.microservices.authorization.models.Credentials;

@Data
public class LoginRequest {
    private Credentials credentials;
}
