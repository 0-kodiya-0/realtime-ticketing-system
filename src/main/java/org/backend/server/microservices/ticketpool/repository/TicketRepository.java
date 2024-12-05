package org.backend.server.microservices.ticketpool.repository;

import jakarta.validation.Valid;
import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.ticketpool.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByVisibleAndDeleted(boolean visible, boolean deleted);
    Ticket findByIdAndVisibleAndDeleted(long id, boolean visible, boolean deleted);
    Ticket findByIdAndVendorAndVisibleAndDeleted(long id, @Valid Vendor vendor, boolean visible, boolean deleted);
}
