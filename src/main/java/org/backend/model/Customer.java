package org.backend.model;

import lombok.Data;
import org.backend.dto.CustomerDto;

import java.util.UUID;

@Data
public class Customer {
    private final String id;
    private boolean isVip;

    public Customer() {
        this.id = UUID.randomUUID().toString();
    }

    public CustomerDto toDto(){
        CustomerDto dto = new CustomerDto();
        dto.setId(id);
        dto.setVip(isVip);
        return dto;
    }
}
