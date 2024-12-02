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
public class Customer extends Person {
    @NotNull
    @Valid
    @Embedded
    @Column(nullable = false)
    private Credentials credentials;

    @Column(name = "isVip", nullable = false)
    private boolean vip = false;

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
