package org.backend.model;

import lombok.Data;
import org.backend.dto.DataToDto;
import org.backend.dto.TicketDto;
import org.backend.enums.TicketCategory;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Data
public class Ticket implements DataToDto<TicketDto> {
    private final Vendor vendor;
    private final long quantity;
    private final ReentrantLock lock = new ReentrantLock();
    private final String id;
    private long boughtQuantity;
    private boolean isDeleted = false;

    public Ticket(Vendor vendor, long quantity) {
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

    public void deleted() {
        if (!this.isDeleted) {
            this.isDeleted = true;
        }
    }

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
