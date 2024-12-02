package org.backend.server.microservices.authorization.configuration;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MissingClaimException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.backend.server.microservices.authorization.dto.AuthenticationToken;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InvalidClassException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public boolean checkAccessLevelsExists(String accessLevel) {
        for (AccessLevel accessLevelEnum : AccessLevel.values()) {
            if (accessLevelEnum.equals(AccessLevel.valueOf(accessLevel))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException, ExpiredJwtException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);
        Jws<?> rawJwt = jwtUtil.extractToken(jwt);
        AuthenticationToken authenticationToken = new AuthenticationToken(jwt, rawJwt);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        if (authenticationToken.getAccessLevel() == null) {
            throw new MissingClaimException(rawJwt.getHeader(), authenticationToken.getClaims(), "ACCESS_LEVEL is missing");
        } else if (checkAccessLevelsExists(authenticationToken.getAccessLevel())) {
            throw new InvalidClassException("ACCESS_LEVEL is invalid");
        } else {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        }
    }
}
