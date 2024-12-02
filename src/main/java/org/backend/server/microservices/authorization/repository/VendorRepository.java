package org.backend.server.microservices.authorization.repository;

import org.backend.server.microservices.authorization.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    boolean existsByCredentialsUserNameContaining(String userName);

    Vendor findByCredentialsUserNameContaining(String userName);

    Optional<Vendor> findById(Long id);
}
