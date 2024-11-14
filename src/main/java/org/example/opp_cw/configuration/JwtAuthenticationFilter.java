package org.example.opp_cw.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.opp_cw.dto.CustomAuthenticationToken;
import org.example.opp_cw.enums.AccessLevel;
import org.example.opp_cw.enums.TokenType;
import org.example.opp_cw.model.Credentials;
import org.example.opp_cw.services.AdminService;
import org.example.opp_cw.services.CustomerService;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;
    private final AdminService adminService;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil, CustomerService customerService, AdminService adminService) {
        this.jwtUtil = jwtUtil;
        this.customerService = customerService;
        this.adminService = adminService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException, ExpiredJwtException {
        final String authHeader = request.getHeader("Authorization");

        System.out.println(authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        Claims jwtClaims = jwtUtil.extractClaims(jwt);
        String tokenType = jwtClaims.get("TOKEN_TYPE", String.class);
        String accessLevel = jwtClaims.get("ACCESS_LEVEL", String.class);
        String userName = jwtClaims.get("USER_NAME", String.class);
        String tokenId = jwtClaims.get("TOKEN_ID", String.class);
        List<GrantedAuthority> authoritys;
        if (tokenType == null) {
            throw new JwtException("Invalid JWT token");
        } else {
            if (tokenId == null) {
                throw new JwtException("Invalid JWT token");
            } else if (tokenType.equals(TokenType.SIGNUP_TOKEN.name())) {
                authoritys = List.of(new SimpleGrantedAuthority(AccessLevel.SIGNUP.name()));
            } else if (tokenType.equals(TokenType.LOGIN_TOKEN.name())) {
                authoritys = List.of(new SimpleGrantedAuthority(AccessLevel.LOGIN.name()));
            } else if (accessLevel != null && userName != null) {
                if (accessLevel.equals(AccessLevel.ADMIN.name())) {
                    Credentials credentials = adminService.isAdmin(userName);
                    if (credentials == null) {
                        throw new JwtException("Invalid username");
                    }
                    authoritys = credentials.getAuthority();
                } else if (accessLevel.equals(AccessLevel.CUSTOMER.name())) {
                    Credentials credentials = customerService.isCustomer(userName);
                    if (credentials == null) {
                        throw new JwtException("Invalid username");
                    }
                    authoritys = credentials.getAuthority();
                } else {
                    throw new JwtException("Invalid access level");
                }
            } else {
                throw new JwtException("Invalid JWT token");
            }
        }
        CustomAuthenticationToken authToken = new CustomAuthenticationToken(null, null, authoritys, jwtClaims);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
