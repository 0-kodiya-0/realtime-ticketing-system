package org.backend.server.configuration;

import org.backend.server.microservices.authorization.dto.AuthenticationToken;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.authorization.services.CustomerService;
import org.backend.server.microservices.authorization.services.VendorService;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationProvider;
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
        Customer customer = customerService.findCustomer(userName);
        if (customer == null) {
            throw new UsernameNotFoundException("Invalid username");
        }
        return customer;
    }

    public Vendor vendorCredentialCheck(String userName) throws UsernameNotFoundException {
        Vendor vendor = vendorService.findVendor(userName);
        if (vendor == null) {
            throw new UsernameNotFoundException("Invalid username");
        }
        return vendor;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof AuthenticationToken authenticationToken) {
            if (authenticationToken.getAccessLevel().equals(AccessLevel.SIGNUP)) {
                authenticationToken.addAuthorities(AccessLevel.SIGNUP.name());
            } else if (authenticationToken.getAccessLevel().equals(AccessLevel.LOGIN)) {
                authenticationToken.addAuthorities(AccessLevel.LOGIN.name());
            } else if (authenticationToken.getAccessLevel().equals(AccessLevel.CUSTOMER)) {
                Customer customer = customerCredentialCheck(authenticationToken.getName());
                authenticationToken.setCredentials(customer.getCredentials());
                authenticationToken.setAuthorities(customer.getCredentials().getAuthority());
                customer.setCredentials(null);
                authenticationToken.setPrincipal(customer);
                authenticationToken.clearCredentialsPassword();
            } else if (authenticationToken.getAccessLevel().equals(AccessLevel.VENDOR)) {
                Vendor vendor = vendorCredentialCheck(authenticationToken.getUserName());
                authenticationToken.setCredentials(vendor.getCredentials());
                authenticationToken.setAuthorities(vendor.getCredentials().getAuthority());
                vendor.setCredentials(null);
                authenticationToken.setPrincipal(vendor);
                authenticationToken.clearCredentialsPassword();
            }
            authenticationToken.setAuthenticated(true);
            return authenticationToken;
        }
        throw new AuthorizationServiceException("Server error");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
