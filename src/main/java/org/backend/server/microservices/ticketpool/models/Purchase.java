package org.backend.server.microservices.ticketpool.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.ticketpool.enums.PurchaseStatus;

import java.time.LocalDateTime;

@Entity
@Data
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    @NotNull
    @Valid
    @ManyToOne(targetEntity = Ticket.class)
    @JoinColumn(name = "ticket", referencedColumnName = "ticketId", nullable = false, updatable = false)
    private Ticket ticket;

    @NotNull
    @Valid
    @ManyToOne(targetEntity = Customer.class)
    @JoinColumn(name = "customer", referencedColumnName = "id", nullable = false, updatable = false)
    private Customer customer;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime purchaseDate;

    @NotNull
    private PurchaseStatus purchaseStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
