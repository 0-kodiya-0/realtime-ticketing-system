package org.backend.server.microservices.authorization.repository;

import org.backend.server.microservices.authorization.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCredentialsUserNameContaining(String userName);
    Customer findByCredentialsUserNameContaining(String userName);
    Optional<Customer> findById(Long id);
}
