package org.example.opp_cw.controller;


import jakarta.validation.Valid;
import org.example.opp_cw.dto.requestbody.SignUpRequest;
import org.example.opp_cw.enums.UsersType;
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
    public ResponseEntity<Boolean> signupCustomer(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(signUpService.signUp(UsersType.CUSTOMER, signUpRequest));
    }

}
