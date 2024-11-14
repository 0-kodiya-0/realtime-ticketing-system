package org.example.opp_cw.repository.admin;

import org.bson.types.ObjectId;
import org.example.opp_cw.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    @Query(value = "{ '_id' : ?0 }", fields = "{ '_id' : 1 }", exists = true)
    boolean existsById(ObjectId id);

    @Query(value = "{ '_id' : ?0 }", fields = "{ '_id' : 1 }")
    Admin findById(ObjectId id);

    @Query(value = "{ '_id' : ?0, 'isSystemAuthorized': true, 'isVisible': true }", exists = true)
    boolean isAdminVerified(ObjectId id);
}
