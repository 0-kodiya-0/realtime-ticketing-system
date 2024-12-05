package org.backend.server.microservices.authorization.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.backend.server.annotations.IsRegexValid;

import java.util.Date;

@Entity
@Data
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(nullable = false, unique = true)
    @IsRegexValid(regexp = "^[a-zA-Z\s]{5,20}$")
    private String vendorName;

    @Column(name = "isVip", nullable = false)
    private boolean vip = false;

    @Column(name = "isVisible", nullable = false)
    private boolean visible = false;

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
