package org.backend.model;

import lombok.Data;
import org.backend.dto.DataToDto;
import org.backend.dto.VendorDto;

import java.util.UUID;

/**
 * Represents a vendor entity.
 * This class encapsulates the properties and behavior of a vendor.
 * It implements the {@link DataToDto} interface, allowing conversion to a {@link VendorDto} object.
 */
@Data
public class Vendor implements DataToDto<VendorDto> {
    private final String id;

    public Vendor() {
        // Random UUID generate to create unique id for each vendor instance
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public VendorDto toDto() {
        VendorDto dto = new VendorDto();
        dto.setId(this.id);
        return dto;
    }
}
