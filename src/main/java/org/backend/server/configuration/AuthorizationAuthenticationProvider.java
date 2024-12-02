package org.backend.server.configuration;

import org.backend.server.microservices.authorization.dto.AuthenticationToken;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Credentials;
import org.backend.server.microservices.authorization.services.CustomerService;
import org.backend.server.microservices.authorization.services.VendorService;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizationAuthenticationProvider implements AuthenticationProvider {

    private final CustomerService customerService;
    private final VendorService vendorService;

    public AuthorizationAuthenticationProvider(CustomerService customerService, VendorService vendorService) {
        this.customerService = customerService;
        this.vendorService = vendorService;
    }

    public List<GrantedAuthority> customerCredentialCheck(String userName) throws UsernameNotFoundException {
        Credentials credentials = customerService.findCustomer(userName).getCredentials();
        if (credentials == null) {
            throw new UsernameNotFoundException("Invalid username");
        }
        return credentials.getAuthority();
    }

    public List<GrantedAuthority> vendorCredentialCheck(String userName) throws UsernameNotFoundException {
        Credentials credentials = vendorService.findVendor(userName).getCredentials();
        if (credentials == null) {
            throw new UsernameNotFoundException("Invalid username");
        }
        return credentials.getAuthority();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof AuthenticationToken authenticationToken) {
            if (authenticationToken.getAccessLevel().equals(AccessLevel.SIGNUP.name())) {
                authenticationToken.addAuthorities(AccessLevel.SIGNUP.name());
            } else if (authenticationToken.getAccessLevel().equals(AccessLevel.LOGIN.name())) {
                authenticationToken.addAuthorities(AccessLevel.LOGIN.name());
            } else if (authenticationToken.getAccessLevel().equals(AccessLevel.CUSTOMER.name())) {
                authenticationToken.setAuthorities(customerCredentialCheck(authenticationToken.getUserName()));
            } else if (authenticationToken.getAccessLevel().equals(AccessLevel.VENDOR.name())) {
                authenticationToken.setAuthorities(vendorCredentialCheck(authenticationToken.getUserName()));
            }
            return authenticationToken;
        }
        throw new AuthorizationServiceException("Server error");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthenticationToken.class.isAssignableFrom(authentication);
    }
}
