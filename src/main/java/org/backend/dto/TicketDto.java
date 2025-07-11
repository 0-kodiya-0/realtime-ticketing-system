package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Define the values that need to be saved in the files and send through http requests.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TicketDto extends Dto {
    private String vendorId;
    private long quantity;
    private long boughtQuantity;
}
