package org.backend.services;

import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.backend.enums.AccessLevel;
import org.backend.model.Contact;
import org.backend.model.Credentials;
import org.backend.model.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MongoTemplate customerMongoTemplate;

    public CustomerService(BCryptPasswordEncoder bCryptPasswordEncoder, @Qualifier("customerMongoTemplate") MongoTemplate customerMongoTemplate) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customerMongoTemplate = customerMongoTemplate;
    }

    public ObjectId saveCustomer(Customer customer) {
        ObjectId id = ObjectId.get();
        customer.set_id(id);
        customerMongoTemplate.insert(customer);
        return id;
    }

    public void saveCustomerCredentials(ObjectId objectId, Credentials credentials) {
        credentials.set_id(objectId);
        credentials.setAuthority(List.of(new SimpleGrantedAuthority(AccessLevel.CUSTOMER.name())));
        credentials.setPassword(bCryptPasswordEncoder.encode(credentials.getPassword()));
        customerMongoTemplate.insert(credentials);
    }

    public void saveCustomerContact(ObjectId objectId, Contact contact) {
        contact.set_id(objectId);
        customerMongoTemplate.insert(contact);
    }

    public boolean verifyCustomer(ObjectId objectId) {
        UpdateResult updateResult = customerMongoTemplate.updateFirst(
                new Query(
                        new Criteria()
                                .andOperator(
                                        Criteria.where("_id").is(objectId),
                                        Criteria.where("isSystemAuthorized").is(false),
                                        Criteria.where("isVisible").is(false)
                                )
                ),
                new Update()
                        .set("isSystemAuthorized", true)
                        .set("isVisible", true),
                Customer.class);
        return updateResult.getMatchedCount() == 1;
    }

    public boolean isCustomer(ObjectId id) {
        return customerMongoTemplate.findById(id, Customer.class) != null && customerMongoTemplate.findById(id, Credentials.class) != null && customerMongoTemplate.findById(id, Contact.class) != null;
    }

    public boolean isCustomerVerified(ObjectId id) {
        return customerMongoTemplate.exists(
                new Query(
                        new Criteria()
                                .andOperator(
                                        Criteria.where("_id").is(id),
                                        Criteria.where("isSystemAuthorized").is(true),
                                        Criteria.where("isVisible").is(true)
                                )
                ), Customer.class);
    }

    public Credentials isCustomer(String userName) {
        return customerMongoTemplate.findOne(new Query(Criteria.where("userName").is(userName)), Credentials.class);
    }

    public List<Customer> findAll() {
        return customerMongoTemplate.findAll(Customer.class);
    }
}
