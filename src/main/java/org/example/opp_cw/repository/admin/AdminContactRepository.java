package org.example.opp_cw.repository.admin;

import org.example.opp_cw.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminContactRepository extends MongoRepository<Contact, String> {
}
