package org.backend.server.microservices.authorization.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.backend.server.dto.ApiResponse;
import org.backend.server.microservices.authorization.configuration.JwtUtil;
import org.backend.server.microservices.authorization.dto.AuthenticationToken;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.models.Vendor;
import org.backend.server.microservices.authorization.services.SignUpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/signup")
public class SignUpController {
    private final JwtUtil jwtUtil;
    private final SignUpService signUpService;

    public SignUpController(final JwtUtil jwtUtil, final SignUpService signUpService) {
        this.jwtUtil = jwtUtil;
        this.signUpService = signUpService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getSignupToken(HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + jwtUtil.buildToken(
                "Signup token", AccessLevel.SIGNUP.name()));
        return new ApiResponse(HttpStatus.CREATED, "Token generate successfully").createResponse();
    }

    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> customerSignup(@Valid @RequestBody Customer customer) {
        signUpService.signupCustomer(customer);
        return new ApiResponse(HttpStatus.CREATED, "Customer created successfully").createResponse();
    }

    @PostMapping("/becomevendor")
    public void vendorSignup(@Valid @RequestBody Vendor vendor, HttpServletResponse response) throws AccountNotFoundException, IOException {
        AuthenticationToken authenticationToken = (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        signUpService.signupVendor(vendor, (Customer) authenticationToken.getPrincipal());
        response.sendRedirect("/login/becomevendor");
    }
}
