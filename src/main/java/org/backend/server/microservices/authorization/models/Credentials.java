package org.backend.server.microservices.authorization.models;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.annotations.IsRegexValid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

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

    @Column(nullable = false)
    @ElementCollection
    private Collection<String> authority;

    public Collection<? extends GrantedAuthority> getAuthority() {
        return authority.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(ArrayList::new));
    }

    public void setAuthority(@NotNull final Collection<GrantedAuthority> authority) {
        this.authority = authority.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toCollection(ArrayList::new));
    }

    public Collection<String> getAuthorityAsString() {
        return this.authority;
    }

    public void setAuthorityAsString(@NotNull final Collection<String> authority) {
        this.authority = authority;
    }
}
