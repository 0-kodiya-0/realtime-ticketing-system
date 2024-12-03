package org.backend.server.microservices.authorization.dto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import lombok.Getter;
import lombok.Setter;
import org.backend.server.microservices.authorization.models.Credentials;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class AuthenticationToken implements Authentication {
    private String token;
    private Header<?> tokenHeader;
    private Claims claims;
    private String accessLevel;
    private String userName;
    private Object principal;
    private Object credentials;
    private Object details;
    private Collection<GrantedAuthority> authorities;
    private boolean authenticated;

    public AuthenticationToken(String token, Jws<?> rawToken) {
        this.token = token;
        this.tokenHeader = rawToken.getHeader();
        this.claims = (Claims) rawToken.getBody();
        this.accessLevel = claims.get("ACCESS_LEVEL", String.class);
        this.userName = claims.get("USER_NAME", String.class);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void addAuthorities(GrantedAuthority authorities) {
        this.authorities.add(authorities);
    }

    public void addAuthorities(String authorities) {
        this.authorities.add(new SimpleGrantedAuthority(authorities));
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public void clearCredentialsPassword() {
        Credentials credentials = (Credentials) this.credentials;
        credentials.setPassword(null);
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userName;
    }
}
