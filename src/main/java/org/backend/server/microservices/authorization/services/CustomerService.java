package org.backend.server.microservices.authorization.services;

import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
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
        customer.setVisible(true);
        customer.setSystemAuthorized(true);
        customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer findCustomer(String username) {
        Customer customer = customerRepository.findByCredentialsUserName(username);
        if (customer == null) {
            return null;
        }
        customer.getCredentials().getAuthority();
        return customer;
    }

    public Customer findCustomer(long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer updateAuthorities(long id, String... authorities) {
        Customer customer = findCustomer(id);
        Collection<String> grantedAuthorities = customer.getCredentials().getAuthorityAsString();
        grantedAuthorities.addAll(Arrays.asList(authorities));
        customer.getCredentials().setAuthorityAsString(grantedAuthorities);
        customerRepository.save(customer);
        return customer;
    }

    @Override
    public boolean exists(String username) {
        return customerRepository.existsByCredentialsUserName(username);
    }

    @Override
    public boolean exists(long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public boolean isVerified(String username) {
        return customerRepository.existsByCredentialsUserNameAndVisibleTrueAndSystemAuthorizedTrueAndDeletedFalse(username);
    }

    @Override
    public boolean isVerified(long id) {
        return customerRepository.existsByIdAndVisibleTrueAndSystemAuthorizedTrueAndDeletedFalse(id);
    }

}
