package org.backend.server.microservices.authorization.services;

import org.backend.server.microservices.authorization.dto.LoginRequest;
import org.backend.server.microservices.authorization.models.Customer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Customer loginCustomer(LoginRequest loginRequest) throws AccountException, BadCredentialsException {
        Customer customer = customerService.findCustomer(loginRequest.getCredentials().getUserName());
        if (customer == null || !customer.isSystemAuthorized() || !customer.isVisible() || customer.isDeleted()) {
            throw new AccountNotFoundException("Invalid customer details");
        }
        if (!loginRequest.getCredentials().getPassword().matches("^[a-zA-Z0-9@_*.]{5,20}$")) {
            throw new CompromisedPasswordException("double check the format");
        }
        if (!bCryptPasswordEncoder.matches(loginRequest.getCredentials().getPassword(), customer.getCredentials().getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return customer;
    }

    public void loginVendor(Customer customer) throws AccountException {
        if (!vendorService.exists(customer.getId())) {
            throw new AccountNotFoundException("Vendor not linked to customer");
        }
    }
}
