package org.example.opp_cw.services;

import org.example.opp_cw.model.Contact;
import org.example.opp_cw.model.Credentials;
import org.example.opp_cw.model.Customer;
import org.example.opp_cw.repository.ContactRepository;
import org.example.opp_cw.repository.CredentialsRepository;
import org.example.opp_cw.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private CredentialsRepository credentialsRepository;

    public void saveCustomer(Customer customer, Credentials credentials, Contact contact) {
        customer = customerRepository.insert(customer);
        credentials.setOwnerId(customer.getId());
        contact.setOwnerId(customer.getId());
        credentialsRepository.insert(credentials);
        contactRepository.insert(contact);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
