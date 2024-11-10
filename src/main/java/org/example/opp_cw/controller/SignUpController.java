package org.example.opp_cw.controller;


import jakarta.validation.Valid;
import org.example.opp_cw.dto.requestbody.SignUpRequest;
import org.example.opp_cw.enums.UsersType;
import org.example.opp_cw.services.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("/customer")
    public ResponseEntity<Boolean> signupCustomer(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(signUpService.signUp(UsersType.CUSTOMER, signUpRequest));
    }
}
