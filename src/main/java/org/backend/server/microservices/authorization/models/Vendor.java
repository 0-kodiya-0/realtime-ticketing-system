package org.backend.server.microservices.authorization.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Vendor extends Person {
    @NotNull
    @Column(nullable = false, unique = true)
    private String vendorName;

    @NotNull
    @Column(nullable = false)
    private String vendorType;

    @NotNull
    @Valid
    @Embedded
    @Column(nullable = false)
    private Credentials credentials;

    @Column(name = "isVerified", nullable = false)
    private boolean verified = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;  // Set updatedAt to createdAt initially
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();  // Update the updatedAt field on update
    }
}
