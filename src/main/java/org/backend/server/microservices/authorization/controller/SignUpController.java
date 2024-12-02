package org.backend.server.microservices.authorization.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.backend.server.dto.ApiResponse;
import org.backend.server.microservices.authorization.configuration.JwtUtil;
import org.backend.server.microservices.authorization.dto.SignUpRequest;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.services.SignUpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        response.addHeader("Authorization", "Bearer " + jwtUtil.buildToken(AccessLevel.LOGIN.name(), "Signup token"));
        return new ApiResponse(HttpStatus.CREATED, "Token generate successfully").createResponse();
    }

    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> customerSignup(@Valid @RequestBody SignUpRequest signUpRequest){
        signUpService.signupCustomer(signUpRequest);
        return new ApiResponse(HttpStatus.CREATED, "Customer created successfully").createResponse();
    }

    @PostMapping("/vendor")
    public ResponseEntity<ApiResponse> vendorSignup(@Valid @RequestBody SignUpRequest signUpRequest){
        signUpService.signupVendor(signUpRequest);
        return new ApiResponse(HttpStatus.CREATED, "Vendor created successfully").createResponse();
    }
}
