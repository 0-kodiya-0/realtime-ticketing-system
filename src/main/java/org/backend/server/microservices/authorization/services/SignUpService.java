package org.backend.server.microservices.authorization.services;

import jakarta.persistence.EntityExistsException;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.models.Vendor;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Collection;

@Service
public class SignUpService {
    private final CustomerService customerService;
    private final VendorService vendorService;

    public SignUpService(CustomerService customerService, VendorService vendorService) {
        this.customerService = customerService;
        this.vendorService = vendorService;
    }

    @Transactional
    public void signupCustomer(Customer customer) throws NullPointerException {
        if (customerService.exists(customer.getCredentials().getUserName())) {
            throw new EntityExistsException("Customer already exists");
        }

        if (!customer.getCredentials().getPassword().matches("^[a-zA-Z0-9@_*.]{5,20}$")) {
            throw new CompromisedPasswordException("double check the format");
        }
        customerService.save(customer);
    }

    @Transactional
    public void signupVendor(Vendor vendor, Customer customer) throws NullPointerException, AccountNotFoundException {
        if (vendorService.exists(customer.getId())) {
            throw new EntityExistsException("Vendor assigned to customer already exists");
        }
        vendorService.save(vendor);
        customerService.updateAuthorities(customer.getId(), AccessLevel.VENDOR.name());
    }
}
