package org.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.backend.configuration.JwtUtil;
import org.backend.dto.requestbody.LoginRequest;
import org.backend.dto.responsebody.ApiResponse;
import org.backend.enums.AccessLevel;
import org.backend.enums.TokenType;
import org.backend.services.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<ApiResponse> getLoginToken(HttpServletRequest request, HttpServletResponse response) {
        String jwtToken = jwtUtil.buildToken(Map.of("TOKEN_TYPE", TokenType.LOGIN_TOKEN.name(), "TOKEN_ID", ObjectId.get().toHexString()), "Signup token");
        response.addHeader("Authorization", "Bearer " + jwtToken);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "signup token send", request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> loginCustomer(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        ApiResponse apiResponse = loginService.loginCustomer(loginRequest, request);
        if (apiResponse.getStatus() == HttpStatus.OK) {
            String jwtToken = jwtUtil.buildToken(Map.of("TOKEN_TYPE", TokenType.CUSTOMER_TOKEN.name(), "TOKEN_ID", ObjectId.get().toHexString(), "USER_NAME", loginRequest.getCredentials().getUserName(), "ACCESS_LEVEL", AccessLevel.CUSTOMER.name()), "Signup token");
            response.addHeader("Authorization", "Bearer " + jwtToken);
        }
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse> loginAdmin(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        ApiResponse apiResponse = loginService.loginAdmin(loginRequest, request);
        if (apiResponse.getStatus() == HttpStatus.OK) {
            String jwtToken = jwtUtil.buildToken(Map.of("TOKEN_TYPE", TokenType.ADMIN_TOKEN.name(), "TOKEN_ID", ObjectId.get().toHexString(), "USER_NAME", loginRequest.getCredentials().getUserName(), "ACCESS_LEVEL", AccessLevel.ADMIN.name()), "Signup token");
            response.addHeader("Authorization", "Bearer " + jwtToken);
        }
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
}
