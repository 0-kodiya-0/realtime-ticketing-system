package org.backend.server.configuration;

import org.backend.server.microservices.authorization.configuration.JwtAuthenticationFilter;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class EndpointsSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthorizationAuthenticationProvider authorizationAuthenticationProvider;

    public EndpointsSecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthorizationAuthenticationProvider authorizationAuthenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authorizationAuthenticationProvider = authorizationAuthenticationProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain signUpEndpoints(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).securityMatcher("/signup/**")
                .authorizeHttpRequests((req) -> req
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/signup/**").hasAuthority(AccessLevel.SIGNUP.name())
                )
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authorizationAuthenticationProvider);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain loginUpEndpoints(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).securityMatcher("/login/**")
                .authorizeHttpRequests((req) -> req
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/login/**").hasAuthority(AccessLevel.LOGIN.name())
                )
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authorizationAuthenticationProvider);
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain ticketOperationsEndpoints(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).securityMatcher("/ticket/**")
                .authorizeHttpRequests((req) -> req
                        .requestMatchers("/ticket").denyAll()
                        .requestMatchers("/ticket/add", "/ticket/remove").hasAuthority(AccessLevel.VENDOR.name())
                        .requestMatchers("/ticket/buy", "ticket/que").hasAuthority(AccessLevel.CUSTOMER.name())
                )
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authorizationAuthenticationProvider);
        return http.build();
    }
}
