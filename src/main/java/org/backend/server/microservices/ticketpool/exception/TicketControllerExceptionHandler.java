package org.backend.server.microservices.ticketpool.exception;

import org.backend.server.dto.ApiResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(3)
@ControllerAdvice(basePackages = "org.backend.server.microservices.ticketpool.controller")
public class TicketControllerExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return new ApiResponse(HttpStatus.BAD_REQUEST, ex.getMessage()).createResponse();
    }
}
