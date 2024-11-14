package org.example.opp_cw.configuration;

import org.example.opp_cw.enums.AccessLevel;
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

    public EndpointsSecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain signUpEndpoints(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).securityMatcher("/signup/**")
                .authorizeHttpRequests((req) -> req
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/signup/**").hasAuthority(AccessLevel.SIGNUP.name()))
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain loginUpEndpoints(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).securityMatcher("/login/**")
                .authorizeHttpRequests((req) -> req
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/login/**").hasAuthority(AccessLevel.LOGIN.name()))
                .sessionManagement((session) -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
