package org.backend.server.microservices.authorization.models;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.annotations.IsRegexValid;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Embeddable
@Data
public class Credentials {
    @NotNull
    @IsRegexValid(regexp = "^[a-zA-Z0-9@_*.]+$")
    @Column(nullable = false, unique = true)
    private String userName;

    @NotNull
    @Column(nullable = false, unique = true)
    private String password;

    @NotNull
    @Column(nullable = false)
    @ElementCollection
    private List<GrantedAuthority> authority;
}
