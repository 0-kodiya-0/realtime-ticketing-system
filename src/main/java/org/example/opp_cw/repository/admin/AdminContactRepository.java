package org.example.opp_cw.repository.admin;

import org.bson.types.ObjectId;
import org.example.opp_cw.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminContactRepository extends MongoRepository<Contact, String> {
    @Query(value = "{ '_id' : ?0 }", fields = "{ '_id' : 1 }")
    Contact findById(ObjectId id);
}
