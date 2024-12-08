package org.backend.dto;

import lombok.Data;
import org.backend.enums.PurchaseStatus;

import java.util.Date;

@Data
public class PurchaseDto {
    private String id;
    private TicketDto ticket;
    private CustomerDto customer;
    private Date purchaseDate;
    private PurchaseStatus purchaseStatus;
}
