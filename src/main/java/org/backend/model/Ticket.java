package org.backend.model;

import lombok.Data;
import org.backend.dto.DataToDto;
import org.backend.dto.TicketDto;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Represents a ticket made and add by a vendor.
 * This class encapsulates the relationship between a ticket and vendor.
 * It implements the {@link DataToDto} interface, allowing conversion to a {@link TicketDto} object.
 */
@Data
public class Ticket implements DataToDto<TicketDto> {
    private final Vendor vendor;
    private final long quantity;
    private final ReentrantLock lock = new ReentrantLock();
    private final String id;
    private long boughtQuantity;
    private boolean isDeleted = false;

    public Ticket(Vendor vendor, long quantity) {
        // Random UUID generate to create unique id for each vendor instance
        this.id = UUID.randomUUID().toString();
        this.vendor = vendor;
        this.quantity = quantity;
    }

    public boolean isBoughtQuantityReachedMaxQuantity() {
        return boughtQuantity >= quantity;
    }

    public void increaseBoughtQuantity() {
        boughtQuantity++;
    }

    /**
     * Marks the object as deleted. If it is not already marked as deleted, it sets the {@code isDeleted} flag to true.
     */
    public void deleted() {
        if (!this.isDeleted) {
            this.isDeleted = true;
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
    public TicketDto toDto() {
        TicketDto dto = new TicketDto();
        dto.setId(id);
        dto.setVendorId(vendor.getId());
        dto.setQuantity(quantity);
        dto.setBoughtQuantity(boughtQuantity);
        return dto;
    }
}
