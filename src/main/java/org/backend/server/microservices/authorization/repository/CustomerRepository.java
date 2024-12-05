package org.backend.server.microservices.authorization.repository;

import org.backend.server.microservices.authorization.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCredentialsUserName(String userName);

    Customer findByCredentialsUserName(String userName);

    Optional<Customer> findById(Long id);

    @Modifying
    @Query("UPDATE Customer e SET e.credentials.authority = ?2  WHERE e.id = ?1")
    int updateByIdAndCredentialsAuthority(Long id, List<String> authority);
}
