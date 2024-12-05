package org.backend.server.microservices.authorization.models;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.annotations.IsRegexValid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Embeddable
@Data
public class Credentials {
    @NotNull
    @IsRegexValid(regexp = "^[a-zA-Z0-9@_*.]{5,20}$")
    @Column(nullable = false, unique = true)
    private String userName;

    @NotNull
    @Column(nullable = false, unique = true)
    private String password;

    @NotNull
    @Column(nullable = false)
    @ElementCollection
    private Collection<String> authority;

    public @NotNull Collection<? extends GrantedAuthority> getAuthority() {
        return authority.stream().map(SimpleGrantedAuthority::new).toList();
    }

    public void setAuthority(@NotNull final Collection<GrantedAuthority> authority) {
        this.authority = authority.stream().map(GrantedAuthority::getAuthority).toList();
    }
}
