package org.example.opp_cw.services;

import org.example.opp_cw.model.Customer;
import org.example.opp_cw.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void saveCustomer(Customer entity) {
        customerRepository.save(entity);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
