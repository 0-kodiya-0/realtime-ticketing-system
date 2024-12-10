package org.backend.server.controller;

import org.backend.dto.TicketDto;
import org.backend.model.Ticket;
import org.backend.pools.TicketPool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketPool ticketPool;

    public TicketController(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @GetMapping("/all-tickets")
    public List<TicketDto> allTickets() {
        return ticketPool.getInUseObjects().stream().map(object -> (Ticket) object).map(Ticket::toDto).toList();
    }

    @GetMapping("/quantity-not-full")
    public List<Ticket> quantityNotFullTickets() {
        return ticketPool.findAllQuantityNotFullTicket();
    }
}
