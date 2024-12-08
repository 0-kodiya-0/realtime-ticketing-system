package org.backend.model;

import lombok.Data;
import org.backend.dto.VendorDto;

import java.util.UUID;

@Data
public class Vendor {
    private final String id;
    private boolean isDeleted = false;

    public Vendor() {
        this.id = UUID.randomUUID().toString();
    }

    public void deleted() {
        if (!this.isDeleted) {
            this.isDeleted = true;
        }
    }

    public VendorDto toDto() {
        VendorDto dto = new VendorDto();
        dto.setId(this.id);
        return dto;
    }
}
