package org.backend.server.microservices.authorization.services;

import jakarta.persistence.EntityExistsException;
import org.backend.server.microservices.authorization.dto.BecomeVendorRequest;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
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
        customerService.save(customer);
    }

    @Transactional
    public Collection<String> signupVendor(BecomeVendorRequest becomeVendorRequest) throws NullPointerException, AccountNotFoundException {
        if (!customerService.isVerified(becomeVendorRequest.getCustomerId())) {
            throw new AccountNotFoundException("Customer not found or not verified");
        }
        if (vendorService.exists(becomeVendorRequest.getCustomerId())) {
            throw new EntityExistsException("Vendor assigned to customer already exists");
        }
        vendorService.save(becomeVendorRequest.getVendor());
        return customerService.updateAuthorities(becomeVendorRequest.getCustomerId(), AccessLevel.VENDOR.name());
    }
}
