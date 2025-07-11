package org.backend.model;

import lombok.Data;
import org.backend.dto.DataToDto;
import org.backend.dto.PurchaseDto;
import org.backend.enums.PurchaseStatus;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Represents a purchase made by a customer for a ticket.
 * This class encapsulates the relationship between a customer and a ticket in a purchase.
 * It implements the {@link DataToDto} interface, allowing conversion to a {@link PurchaseDto} object.
 */
@Data
public class Purchase implements DataToDto<PurchaseDto> {
    private final Ticket ticket;
    private final Customer customer;
    private final ReentrantLock lock = new ReentrantLock();
    private final String id;
    private Date purchaseDate;
    private PurchaseStatus purchaseStatus;

    public Purchase(Ticket ticket, Customer customer) {
        // Random UUID generate to create unique id for each vendor instance
        this.id = UUID.randomUUID().toString();
        this.ticket = ticket;
        this.customer = customer;
    }

    /**
     * Safely updates the `purchaseStatus` field with a new value using a lock to prevent concurrency issues.
     */
    public PurchaseStatus getPurchaseStatus() {
        lock.lock();
        try {
            return purchaseStatus;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Safely updates the `purchaseDate` field with a new value using a lock to prevent concurrency issues.
     */
    public void setPurchaseDate(Date purchaseDate) {
        lock.lock();
        try {
            this.purchaseDate = purchaseDate;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Locks the object and executes the given function. Once the function completes, the lock is released.
     *
     * @param function Function to execute
     * @param <T> Return type of the function
     * @return Result of the function execution
     */
    public <T> T lockAndExecute(Supplier<T> function) {
        lock.lock();
        try {
            return function.get();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public PurchaseDto toDto() {
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setId(id);
        purchaseDto.setTicketId(ticket.getId());
        purchaseDto.setCustomerId(customer.getId());
        purchaseDto.setPurchaseDate(purchaseDate);
        purchaseDto.setPurchaseStatus(purchaseStatus);
        return purchaseDto;
    }
}
