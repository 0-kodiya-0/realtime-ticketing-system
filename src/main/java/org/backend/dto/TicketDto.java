package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.backend.enums.TicketCategory;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketDto extends Dto {
    private String vendorId;
    private long quantity;
    private long boughtQuantity;
}
