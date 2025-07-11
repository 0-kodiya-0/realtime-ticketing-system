package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.backend.enums.PurchaseStatus;

import java.util.Date;

/**
 * Define the values that need to be saved in the files and send through http requests.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseDto extends Dto {
    private String ticketId;
    private String customerId;
    private Date purchaseDate;
    private PurchaseStatus purchaseStatus;
}
