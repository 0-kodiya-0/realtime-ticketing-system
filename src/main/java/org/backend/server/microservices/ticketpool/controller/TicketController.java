package org.backend.server.microservices.ticketpool.controller;

import jakarta.validation.Valid;
import org.backend.server.dto.ApiResponse;
import org.backend.server.microservices.ticketpool.models.Ticket;
import org.backend.server.microservices.ticketpool.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/getAll")
    @ResponseStatus(HttpStatus.OK)
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addTicket(@Valid @RequestBody Ticket ticket) {
        ticketService.addTicket(ticket);
        return new ApiResponse(HttpStatus.OK, "Ticket added successfully").createResponse();
    }

    @GetMapping("/remove")
    public void removeTicket() {

    }

    @RequestMapping("/que")
    public void queTicket() {
    }

    @RequestMapping("/buy")
    public void buyTicket() {
    }
}
