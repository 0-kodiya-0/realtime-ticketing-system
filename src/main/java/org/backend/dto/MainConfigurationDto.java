package org.backend.dto;

import lombok.Data;

@Data
public class MainConfigurationDto {
    private int totalNumberOfTickets;
    private int ticketReleaseRate;
    private int ticketRetrievalRate;
    private int maximumTicketCapacity;
    private int totalNumberOfCustomers;
    private int totalNumberOfVendors;
}
