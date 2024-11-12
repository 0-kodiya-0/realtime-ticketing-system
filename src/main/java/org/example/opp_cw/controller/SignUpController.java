package org.example.opp_cw.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.opp_cw.dto.requestbody.AdminSignUpRequest;
import org.example.opp_cw.dto.requestbody.CustomerSignUpRequest;
import org.example.opp_cw.dto.responsebody.ApiResponse;
import org.example.opp_cw.model.Customer;
import org.example.opp_cw.services.CustomerService;
import org.example.opp_cw.services.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final CustomerService customerService;
    private final SignUpService signUpService;

    public SignUpController(CustomerService customerService, SignUpService signUpService) {
        this.customerService = customerService;
        this.signUpService = signUpService;
    }

    @GetMapping("/customer")
    public ResponseEntity<List<Customer>> getCustomer() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> signupCustomer(@Valid @RequestBody CustomerSignUpRequest customerSignUpRequest, HttpServletRequest request) {
        ApiResponse apiResponse = signUpService.signUp(customerSignUpRequest, request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse> signupAdmin(@Valid @RequestBody AdminSignUpRequest adminSignUpRequest, HttpServletRequest request) {
        ApiResponse apiResponse = signUpService.signUp(adminSignUpRequest, request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

}
