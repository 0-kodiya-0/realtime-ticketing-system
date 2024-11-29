package org.backend.dto.requestbody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.model.Credentials;

@Data
@NoArgsConstructor
public class LoginRequest {
    @NotNull
    @Valid
    private Credentials credentials;
}
