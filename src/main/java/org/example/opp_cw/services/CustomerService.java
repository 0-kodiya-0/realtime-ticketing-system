package org.example.opp_cw.services;

import org.bson.types.ObjectId;
import org.example.opp_cw.model.Contact;
import org.example.opp_cw.model.Credentials;
import org.example.opp_cw.model.Customer;
import org.example.opp_cw.repository.customer.CustomerContactRepository;
import org.example.opp_cw.repository.customer.CustomerCredentialsRepository;
import org.example.opp_cw.repository.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerContactRepository customerContactRepository;
    private final CustomerCredentialsRepository customerCredentialsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomerService(CustomerRepository customerRepository, CustomerContactRepository customerContactRepository, CustomerCredentialsRepository customerCredentialsRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.customerContactRepository = customerContactRepository;
        this.customerCredentialsRepository = customerCredentialsRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void saveCustomer(Customer customer, Credentials credentials, Contact contact) {
        ObjectId id = ObjectId.get();
        customer.set_id(id);
        credentials.setPassword(bCryptPasswordEncoder.encode(credentials.getPassword()));
        credentials.set_id(id);
        contact.set_id(id);
        customerCredentialsRepository.insert(credentials);
        customerContactRepository.insert(contact);
        customerRepository.insert(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
