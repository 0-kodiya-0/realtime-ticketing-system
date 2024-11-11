package org.example.opp_cw.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.opp_cw.dto.requestbody.LoginRequest;
import org.example.opp_cw.dto.responsebody.ApiResponse;
import org.example.opp_cw.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> loginCustomer(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        ApiResponse apiResponse = loginService.login(loginRequest, request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
