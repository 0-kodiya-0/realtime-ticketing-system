package org.backend.repository.customer;

import org.bson.types.ObjectId;
import org.backend.model.Credentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCredentialsRepository extends MongoRepository<Credentials, String> {

    @Query(value = "{ 'userName' : ?0 }", fields = "{ 'userName' : 1 }", exists = true)
    boolean existsByUserName(String username);

    @Query(value = "{ 'userName' : ?0}", fields = "{ 'userName' : 1, 'password': 1, 'authority': 1 }")
    Credentials findByUserName(String userName);

    @Query(value = "{ 'userName' : ?0 }", fields = "{ 'password' : 1 }")
    String findPasswordByUserName(String username);

    @Query(value = "{ '_id' : ?0 }", fields = "{ '_id' : 1 }")
    Credentials findById(ObjectId id);
}
