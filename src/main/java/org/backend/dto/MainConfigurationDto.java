package org.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Define the values that need to be saved in the main configuration file.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MainConfigurationDto extends Dto {
    private int totalNumberOfTickets;
    private int ticketReleaseRate;
    private int ticketRetrievalRate;
    private int maximumTicketCapacity;
}
