package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.backend.enums.PurchaseStatus;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseDto extends Dto {
    private TicketDto ticket;
    private CustomerDto customer;
    private Date purchaseDate;
    private PurchaseStatus purchaseStatus;
}
