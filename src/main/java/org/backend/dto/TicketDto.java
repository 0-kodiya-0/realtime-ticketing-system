package org.backend.dto;

import lombok.Data;
import org.backend.enums.TicketCategory;

@Data
public class TicketDto {
    private String id;
    private VendorDto vendor;
    private TicketCategory category;
    private long quantity;
    private long boughtQuantity;
}
