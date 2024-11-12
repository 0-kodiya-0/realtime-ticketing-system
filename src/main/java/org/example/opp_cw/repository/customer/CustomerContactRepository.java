package org.example.opp_cw.repository.customer;

import org.example.opp_cw.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerContactRepository extends MongoRepository<Contact, String> {
}
