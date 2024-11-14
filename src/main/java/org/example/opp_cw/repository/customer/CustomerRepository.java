package org.example.opp_cw.repository.customer;

import org.bson.types.ObjectId;
import org.example.opp_cw.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    @Query(value = "{ '_id' : ?0 }", fields = "{ '_id' : 1 }", exists = true)
    boolean existsById(ObjectId id);

    @Query(value = "{ '_id' : ?0 }", fields = "{ '_id' : 1 }")
    Customer findById(ObjectId id);

    @Query(value = "{ '_id' : ?0, 'isSystemAuthorized': true, 'isVisible': true }", exists = true)
    boolean isCustomerVerified(ObjectId id);
}
