package org.backend.server.microservices.authorization.configuration;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.backend.server.dto.ApiResponse;
import org.backend.server.microservices.authorization.dto.AuthenticationToken;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public boolean checkAccessLevelsExists(AccessLevel accessLevel) {
        for (AccessLevel accessLevelEnum : AccessLevel.values()) {
            if (accessLevelEnum.equals(accessLevel)) {
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
        try {
            String jwt = authHeader.substring(7);
            Jws<?> rawJwt = jwtUtil.extractToken(jwt);
            AuthenticationToken authenticationToken = new AuthenticationToken(jwt, rawJwt);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            if (authenticationToken.getAccessLevel() == null) {
                throw new MissingClaimException(rawJwt.getHeader(), authenticationToken.getClaims(), "ACCESS_LEVEL is missing");
            } else if (!checkAccessLevelsExists(authenticationToken.getAccessLevel())) {
                throw new IncorrectClaimException(rawJwt.getHeader(), authenticationToken.getClaims(), "ACCESS_LEVEL is invalid");
            } else {
                SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authenticationToken));
                filterChain.doFilter(request, response);
            }
        } catch (JwtException e) {
            System.out.println(e);
            ApiResponse apiResponse = new ApiResponse(HttpStatus.FORBIDDEN, e.getMessage());
            response.setStatus(apiResponse.getStatus().value());
            response.setContentType("application/json");
            response.getWriter().write(apiResponse.createJsonResponse());
        }
    }
}
