package org.backend.server.configuration;

import org.backend.server.microservices.authorization.configuration.JwtAuthenticationFilter;
import org.backend.server.microservices.authorization.configuration.JwtUtil;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.services.CustomerService;
import org.backend.server.microservices.authorization.services.VendorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class EndpointsSecurityConfiguration {

    private final CustomerService customerService;
    private final VendorService vendorService;
    private final JwtUtil jwtUtil;

    public EndpointsSecurityConfiguration(CustomerService customerService, VendorService vendorService, JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.vendorService = vendorService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthorizationAuthenticationProvider(customerService, vendorService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        return new JwtAuthenticationFilter(jwtUtil, authenticationManager);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain endPoints(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .securityMatcher("/signup/**", "/login/**", "/ticket/**")
                .authorizeHttpRequests((req) -> req
                        .requestMatchers("/signup", "/login").permitAll()
                        .requestMatchers("/signup/**").hasAuthority(AccessLevel.SIGNUP.name())
                        .requestMatchers("/login/**").hasAuthority(AccessLevel.LOGIN.name())
                        .requestMatchers("/ticket").denyAll()
                        .requestMatchers("/ticket/add", "/ticket/remove").hasAuthority(AccessLevel.VENDOR.name())
                        .requestMatchers("/ticket/buy", "ticket/que").hasAuthority(AccessLevel.CUSTOMER.name())
                        .requestMatchers("/ticket/get/**").hasAnyAuthority(AccessLevel.CUSTOMER.name(), AccessLevel.VENDOR.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilterBefore(jwtAuthenticationFilter(jwtUtil, authenticationManager), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
