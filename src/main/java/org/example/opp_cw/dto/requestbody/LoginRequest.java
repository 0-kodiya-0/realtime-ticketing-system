package org.example.opp_cw.dto.requestbody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opp_cw.model.Credentials;

@Data
@NoArgsConstructor
public class LoginRequest {
    @NotNull
    @Valid
    private Credentials credentials;
}
