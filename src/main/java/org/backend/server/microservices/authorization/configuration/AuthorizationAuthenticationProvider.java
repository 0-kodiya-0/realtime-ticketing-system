package org.backend.server.microservices.authorization.configuration;

import org.backend.server.microservices.authorization.dto.AuthenticationToken;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.services.CustomerService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthorizationAuthenticationProvider implements AuthenticationProvider {

    private final CustomerService customerService;

    public AuthorizationAuthenticationProvider(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Customer customerCredentialCheck(String userName) throws UsernameNotFoundException {
        Customer customer = customerService.findCustomer(userName);
        if (customer == null) {
            throw new UsernameNotFoundException("Invalid username");
        }
        return customer;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof AuthenticationToken authenticationToken) {
            if (authenticationToken.getAccessLevel().contains(AccessLevel.SIGNUP)) {
                authenticationToken.addAuthorities(AccessLevel.SIGNUP.name());
            } else if (authenticationToken.getAccessLevel().contains(AccessLevel.LOGIN)) {
                authenticationToken.addAuthorities(AccessLevel.LOGIN.name());
            } else if (authenticationToken.getAccessLevel().contains(AccessLevel.CUSTOMER)) {
                Customer customer = customerCredentialCheck(authenticationToken.getName());
                authenticationToken.setCredentials(customer.getCredentials());
                authenticationToken.setAuthorities(customer.getCredentials().getAuthority());
                customer.setCredentials(null);
                authenticationToken.setPrincipal(customer);
                authenticationToken.clearCredentialsPassword();
            }
            authenticationToken.setAuthenticated(true);
            return authenticationToken;
        }
        throw new AuthorizationDeniedException("Server error");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
