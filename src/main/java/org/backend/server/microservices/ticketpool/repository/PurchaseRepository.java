package org.backend.server.microservices.ticketpool.repository;

import org.backend.server.microservices.ticketpool.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
