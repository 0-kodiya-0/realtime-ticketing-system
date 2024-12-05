package org.backend.server.microservices.authorization.configuration;

import jakarta.persistence.EntityNotFoundException;
import org.backend.server.dto.ApiResponse;
import org.backend.server.microservices.authorization.dto.AuthenticationToken;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.authorization.services.CustomerService;
import org.backend.server.microservices.authorization.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthorizationAuthenticationProvider implements AuthenticationProvider {

    private final CustomerService customerService;
    private final VendorService vendorService;

    public AuthorizationAuthenticationProvider(CustomerService customerService, VendorService vendorService) {
        this.customerService = customerService;
        this.vendorService = vendorService;
    }

    public Customer customerCredentialCheck(String userName) throws UsernameNotFoundException {
        if (!customerService.isVerified(userName)) {
            throw new UsernameNotFoundException("Invalid username");
        }
        return customerService.findCustomer(userName);
    }

    public void vendorCredentialCheck(long id) throws UsernameNotFoundException {
        if (!vendorService.exists(id)) {
            throw new EntityNotFoundException("Vendor not found");
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            if (authentication instanceof AuthenticationToken authenticationToken) {
                try {
                    if (authenticationToken.getAccessLevel().contains(AccessLevel.SIGNUP)) {
                        authenticationToken.addAuthorities(AccessLevel.SIGNUP.name());
                    } else if (authenticationToken.getAccessLevel().contains(AccessLevel.LOGIN)) {
                        authenticationToken.addAuthorities(AccessLevel.LOGIN.name());
                    } else if (authenticationToken.getAccessLevel().contains(AccessLevel.CUSTOMER)) {
                        Customer customer = customerCredentialCheck(authenticationToken.getName());
                        if (authenticationToken.getAccessLevel().contains(AccessLevel.VENDOR)) {
                            vendorCredentialCheck(customer.getId());
                        }
                        authenticationToken.setCredentials(customer.getCredentials());
                        authenticationToken.setAuthorities(customer.getCredentials().getAuthority());
                        customer.setCredentials(null);
                        authenticationToken.setPrincipal(customer);
                        authenticationToken.clearCredentialsPassword();
                    }
                    authenticationToken.setAuthenticated(true);
                    return authenticationToken;
                } catch (Exception e) {
                    System.out.println(e);
                    authenticationToken.setAuthenticated(false);
                    return authenticationToken;
                }
            }
            throw new AuthorizationDeniedException("Server error");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
