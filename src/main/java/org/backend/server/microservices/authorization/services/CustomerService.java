package org.backend.server.microservices.authorization.services;

import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
public class CustomerService implements UsersDetailsVerify {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(Customer customer) {
        customer.getCredentials().setAuthority(List.of(new SimpleGrantedAuthority(AccessLevel.CUSTOMER.name())));
        customer.getCredentials().setPassword(bCryptPasswordEncoder.encode(customer.getCredentials().getPassword()));
        customerRepository.save(customer);
    }

    public Customer findCustomer(String username) {
        return customerRepository.findByCredentialsUserNameContaining(username);
    }

    public Customer findCustomer(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public boolean exists(String username) {
        return customerRepository.existsByCredentialsUserNameContaining(username);
    }

    @Override
    public boolean exists(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public boolean isVerified(String username) throws AccountException {
        Customer customer = findCustomer(username);
        if (customer == null) {
            throw new AccountException("Verified customer not found");
        }
        return customer.isVisible() && customer.isSystemAuthorized() && customer.isDeleted();
    }

    @Override
    public boolean isVerified(Long id) throws AccountNotFoundException {
        Customer customer = findCustomer(id);
        if (customer == null) {
            throw new AccountNotFoundException("Verified customer not found");
        }
        return customer.isVisible() && customer.isSystemAuthorized() && customer.isDeleted();
    }

    @Override
    public void verifyUser(String username) throws AccountException {
        Customer customer = customerRepository.findByCredentialsUserNameContaining(username);
        verifyUser(customer);
    }

    @Override
    public void verifyUser(Long id) throws AccountException {
        Customer customer = customerRepository.findById(id).orElse(null);
        verifyUser(customer);
    }

    private void verifyUser(Customer customer) throws AccountException {
        if (customer == null) {
            throw new AccountNotFoundException("Customer not found");
        }
        if (customer.isDeleted() || customer.isSystemAuthorized() || customer.isVisible()) {
            throw new AccountException("Customer already verified");
        }
        customer.setVisible(true);
        customer.setSystemAuthorized(true);
        customerRepository.save(customer);
    }
}
