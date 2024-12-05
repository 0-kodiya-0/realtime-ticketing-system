package org.backend.server.microservices.ticketpool.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.ticketpool.enums.TicketCategory;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Valid
    @ManyToOne(targetEntity = Vendor.class)
    @JoinColumn(referencedColumnName = "id", nullable = false, updatable = false)
    private Vendor vendor;

    @NotNull
    @Column(nullable = false)
    private String title;

    private String description;

    @NotNull
    @Column(nullable = false)
    @Min(value = 0)
    private int price;

    @NotNull
    @Column(nullable = false)
    @Min(value = 1)
    private int quantity;

    @Column(nullable = false, columnDefinition = "INTEGER CHECK (quantity >= 0)")
    @Min(value = 0)
    private int boughtQuantity = 0;

    @NotNull
    @Column(nullable = false)
    private TicketCategory category;

    @NotNull
    @Column(nullable = false)
    @ElementCollection
    private List<String> imageUrl;

    @Column(name = "isVisible", nullable = false)
    private boolean visible = true;

    @Column(name = "isDeleted", nullable = false)
    private boolean deleted = false;

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
