package org.backend.server.microservices.ticketpool.controller;

import jakarta.validation.Valid;
import org.backend.server.dto.ApiResponse;
import org.backend.server.microservices.authorization.dto.AuthenticationToken;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.ticketpool.models.Purchase;
import org.backend.server.microservices.ticketpool.models.Ticket;
import org.backend.server.microservices.ticketpool.services.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;

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

    @GetMapping("/remove/{id}")
    public ResponseEntity<ApiResponse> removeTicket(@PathVariable Long id) {
        ticketService.removeTicket(id);
        return new ApiResponse(HttpStatus.NO_CONTENT, "Ticket removed successfully").createResponse();
    }

    @GetMapping("/que/{ticketId}")
    public ResponseEntity<ApiResponse> queTicket(@PathVariable Long ticketId) {
        AuthenticationToken authenticationToken = (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Purchase purchase = ticketService.queTicket(ticketId, (Customer) authenticationToken.getPrincipal());
        return new ApiResponse(HttpStatus.CREATED, Map.of("purchase", purchase.getPurchaseId())).createResponse();
    }

    @GetMapping("/buy/{purchaseId}")
    public ResponseEntity<ApiResponse> buyTicket(@PathVariable Long purchaseId) {
        AuthenticationToken authenticationToken = (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Purchase purchase = ticketService.purchaseTicket(purchaseId, (Customer) authenticationToken.getPrincipal());
        purchase.setCustomer(null);
        return new ApiResponse(HttpStatus.OK, Map.of("purchase", purchase)).createResponse();
    }
}
