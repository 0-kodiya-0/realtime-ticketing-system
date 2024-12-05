package org.backend.server.microservices.authorization.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.backend.server.dto.ApiResponse;
import org.backend.server.microservices.authorization.configuration.JwtUtil;
import org.backend.server.microservices.authorization.dto.LoginRequest;
import org.backend.server.microservices.authorization.enums.AccessLevel;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final JwtUtil jwtUtil;
    private final LoginService loginService;

    public LoginController(LoginService loginService, JwtUtil jwtUtil) {
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getLoginToken(HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + jwtUtil.buildToken("Login token", AccessLevel.LOGIN.name()));
        return new ApiResponse(HttpStatus.CREATED, "Token generate successfully").createResponse();
    }

    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> customerLogin(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) throws AccountException {
        Customer customer = loginService.loginCustomer(loginRequest);
        response.addHeader("Authorization", "Bearer " + jwtUtil.buildTokenWithUsername(customer.getCredentials().getUserName(), "Customer successfully logged in", AccessLevel.CUSTOMER.name()));
        return new ApiResponse(HttpStatus.CREATED, "Token generate successfully").createResponse();
    }
}
