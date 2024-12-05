package org.backend.server.microservices.ticketpool.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.ticketpool.enums.PurchaseStatus;

import java.util.Date;

@Entity
@Data
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Valid
    @ManyToOne(targetEntity = Ticket.class)
    @JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
    private Ticket ticket;

    @NotNull
    @Valid
    @ManyToOne(targetEntity = Customer.class)
    @JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
    private Customer customer;

    private Date purchaseDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Column(nullable = false)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = createdAt;  // Set updatedAt to createdAt initially
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();  // Update the updatedAt field on update
    }
}
