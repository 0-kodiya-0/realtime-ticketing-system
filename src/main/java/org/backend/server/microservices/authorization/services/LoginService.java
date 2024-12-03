package org.backend.server.microservices.authorization.services;

import org.backend.server.microservices.authorization.dto.LoginRequest;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.models.Vendor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;

@Service
public class LoginService {

    private final CustomerService customerService;
    private final VendorService vendorService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginService(CustomerService customerService, VendorService vendorService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerService = customerService;
        this.vendorService = vendorService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Customer loginCustomer(LoginRequest loginRequest) throws AccountException, BadCredentialsException {
        Customer customer = customerService.findCustomer(loginRequest.getCredentials().getUserName());
        if (customer == null || !customer.isSystemAuthorized() || !customer.isVisible() || customer.isDeleted()) {
            throw new AccountNotFoundException("Invalid customer details");
        }
        if (!bCryptPasswordEncoder.matches(loginRequest.getCredentials().getPassword(), customer.getCredentials().getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return customer;
    }

    public Vendor loginVendor(LoginRequest loginRequest) throws AccountException, BadCredentialsException {
        Vendor vendor = vendorService.findVendor(loginRequest.getCredentials().getUserName());
        if (vendor == null || !vendor.isSystemAuthorized() || !vendor.isVisible() || vendor.isDeleted()) {
            throw new AccountNotFoundException("Invalid vendor details");
        }
        if (!bCryptPasswordEncoder.matches(loginRequest.getCredentials().getPassword(), vendor.getCredentials().getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return vendor;
    }
}
