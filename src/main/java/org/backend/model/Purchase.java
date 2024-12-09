package org.backend.model;

import lombok.Data;
import org.backend.dto.DataToDto;
import org.backend.dto.PurchaseDto;
import org.backend.enums.PurchaseStatus;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Data
public class Purchase implements DataToDto<PurchaseDto> {
    private final Ticket ticket;
    private final Customer customer;
    private final ReentrantLock lock = new ReentrantLock();
    private final String id;
    private Date purchaseDate;
    private PurchaseStatus purchaseStatus;

    public Purchase(Ticket ticket, Customer customer) {
        this.id = UUID.randomUUID().toString();
        this.ticket = ticket;
        this.customer = customer;
    }

    public PurchaseStatus getPurchaseStatus() {
        lock.lock();
        try {
            return purchaseStatus;
        } finally {
            lock.unlock();
        }
    }

    public void setPurchaseDate(Date purchaseDate) {
        lock.lock();
        try {
            this.purchaseDate = purchaseDate;
        } finally {
            lock.unlock();
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
    public PurchaseDto toDto() {
        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.setId(id);
        purchaseDto.setTicket(ticket.toDto());
        purchaseDto.setCustomer(customer.toDto());
        purchaseDto.setPurchaseDate(purchaseDate);
        purchaseDto.setPurchaseStatus(purchaseStatus);
        return purchaseDto;
    }
}
