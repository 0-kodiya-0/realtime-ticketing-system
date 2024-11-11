package org.example.opp_cw.services;

import org.bson.types.ObjectId;
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
        ObjectId id = ObjectId.get();
        customer.set_id(id);
        credentials.encodePassword();
        credentials.set_id(id);
        contact.set_id(id);
        credentialsRepository.insert(credentials);
        contactRepository.insert(contact);
        customerRepository.insert(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
