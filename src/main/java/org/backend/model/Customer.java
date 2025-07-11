package org.backend.model;

import lombok.Data;
import org.backend.dto.CustomerDto;
import org.backend.dto.DataToDto;
import org.backend.dto.VendorDto;
import org.backend.enums.CustomerTypes;

import java.util.UUID;

/**
 * Represents a customer entity.
 * This class encapsulates the properties and behavior of a customer.
 * It implements the {@link DataToDto} interface, allowing conversion to a {@link CustomerDto} object.
 */
@Data
public class Customer implements DataToDto<CustomerDto> {
    private final String id;
    /**
     * The type of the customer represented by this object.
     * This field indicates the specific category or customer (VIP , NOT_VIP).
     */
    private CustomerTypes type;

    public Customer() {
        // Random UUID generate to create unique id for each vendor instance
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public CustomerDto toDto() {
        CustomerDto dto = new CustomerDto();
        dto.setId(id);
        dto.setType(type);
        return dto;
    }
}
