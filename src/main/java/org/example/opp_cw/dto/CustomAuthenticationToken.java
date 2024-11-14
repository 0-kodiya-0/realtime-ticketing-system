package org.example.opp_cw.dto;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Setter
@Getter
public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private Claims jwtClaims;

    public CustomAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public CustomAuthenticationToken(Object principal, Object credentials, Claims jwtClaims) {
        super(principal, credentials);
        this.jwtClaims = jwtClaims;
    }

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Claims jwtClaims) {
        super(principal, credentials, authorities);
        this.jwtClaims = jwtClaims;
    }

}
