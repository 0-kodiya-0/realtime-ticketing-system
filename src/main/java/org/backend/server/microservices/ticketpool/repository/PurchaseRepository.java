package org.backend.server.microservices.ticketpool.repository;

import org.backend.server.microservices.ticketpool.enums.PurchaseStatus;
import org.backend.server.microservices.ticketpool.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Purchase findByIdAndPurchaseStatus(long id, PurchaseStatus purchaseStatus);

    List<Purchase> findByPurchaseStatusAndCreatedAtAfter(PurchaseStatus purchaseStatus, Date createdAt);
}
