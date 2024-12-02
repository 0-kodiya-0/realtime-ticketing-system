package org.backend.server.microservices.ticketpool.repository;

import org.backend.server.microservices.ticketpool.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByVisibleTrueAndDeletedFalseOrderByCreatedAtAsc();
    Optional<Ticket> findByTicketIdAndVisibleTrue(Long ticketId);
}
