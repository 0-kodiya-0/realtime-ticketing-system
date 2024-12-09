package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MainConfigurationDto extends Dto {
    private int totalNumberOfTickets;
    private int ticketReleaseRate;
    private int ticketRetrievalRate;
    private int maximumTicketCapacity;
    private int totalNumberOfCustomers;
    private int totalNumberOfVendors;
}
