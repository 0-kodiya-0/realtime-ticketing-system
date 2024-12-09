package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.backend.enums.TicketCategory;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketDto extends Dto {
    private VendorDto vendor;
    private TicketCategory category;
    private long quantity;
    private long boughtQuantity;
}
