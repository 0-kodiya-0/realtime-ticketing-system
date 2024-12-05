package org.backend.server.microservices.authorization.repository;

import org.backend.server.microservices.authorization.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Vendor findById(long id);
}
