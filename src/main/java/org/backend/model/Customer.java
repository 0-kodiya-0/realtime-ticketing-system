package org.backend.model;

import lombok.Data;
import org.backend.dto.CustomerDto;
import org.backend.dto.DataToDto;
import org.backend.enums.CustomerTypes;

import java.util.UUID;

@Data
public class Customer implements DataToDto<CustomerDto> {
    private final String id;
    private CustomerTypes type;

    public Customer() {
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
