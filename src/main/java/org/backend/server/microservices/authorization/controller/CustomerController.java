package org.backend.server.microservices.authorization.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.backend.server.dto.ApiResponse;
import org.backend.server.microservices.authorization.configuration.JwtUtil;
import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.authorization.services.CustomerService;
import org.backend.server.microservices.authorization.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final VendorService vendorService;
    private final JwtUtil jwtUtil;

    public CustomerController(final CustomerService customerService, final VendorService vendorService, JwtUtil jwtUtil) {
        this.customerService = customerService;
        this.vendorService = vendorService;
        this.jwtUtil = jwtUtil;
    }


}
