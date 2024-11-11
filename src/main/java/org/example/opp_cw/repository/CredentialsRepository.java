package org.example.opp_cw.repository;

import org.example.opp_cw.model.Credentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialsRepository extends MongoRepository<Credentials, String> {

    @Query(value = "{ 'userName' : ?0 }", fields = "{ 'userName' : 1 }", exists = true)
    boolean existsByUserName(String username);

    @Query(value = "{ 'userName' : ?0}", fields = "{ 'userName' : 1, password: 1 }")
    Credentials findByUserName(String userName);

    @Query(value = "{ 'userName' : ?0 }", fields = "{ 'password' : 1 }")
    String findPasswordByUserName(String username);
}
