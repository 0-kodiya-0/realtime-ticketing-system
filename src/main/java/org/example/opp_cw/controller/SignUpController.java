package org.example.opp_cw.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.example.opp_cw.configuration.JwtUtil;
import org.example.opp_cw.dto.CustomAuthenticationToken;
import org.example.opp_cw.dto.requestbody.AdminSignUpRequest;
import org.example.opp_cw.dto.requestbody.CustomerSignUpRequest;
import org.example.opp_cw.dto.responsebody.ApiResponse;
import org.example.opp_cw.enums.TokenType;
import org.example.opp_cw.services.SignUpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final SignUpService signUpService;
    private final JwtUtil jwtUtil;

    public SignUpController(SignUpService signUpService, JwtUtil jwtUtil) {
        this.signUpService = signUpService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getSignUpToken(HttpServletRequest request, HttpServletResponse response) {

        String jwtToken = jwtUtil.buildToken(Map.of("TOKEN_TYPE", TokenType.SIGNUP_TOKEN.name(), "TOKEN_ID", ObjectId.get().toHexString()), "Signup token");
        response.addHeader("Authorization", "Bearer " + jwtToken);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "signup token send", request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping("/customer/verify")
    public ResponseEntity<ApiResponse> verifyCustomer(HttpServletRequest request) {
        CustomAuthenticationToken authentication = (CustomAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = signUpService.verifySignUpCustomer((String) authentication.getJwtClaims().get("TOKEN_ID"), request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping("/admin/verify")
    public ResponseEntity<ApiResponse> verifyAdmin(HttpServletRequest request) {
        CustomAuthenticationToken authentication = (CustomAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = signUpService.verifySignUpAdmin((String) authentication.getJwtClaims().get("TOKEN_ID"), request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/customer")
    public ResponseEntity<ApiResponse> signupCustomer(@Valid @RequestBody CustomerSignUpRequest customerSignUpRequest, HttpServletRequest request) {
        CustomAuthenticationToken authentication = (CustomAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = signUpService.signUp((String) authentication.getJwtClaims().get("TOKEN_ID"), customerSignUpRequest, request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @PostMapping("/admin")
    public ResponseEntity<ApiResponse> signupAdmin(@Valid @RequestBody AdminSignUpRequest adminSignUpRequest, HttpServletRequest request) {
        CustomAuthenticationToken authentication = (CustomAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = signUpService.signUp((String) authentication.getJwtClaims().get("TOKEN_ID"), adminSignUpRequest, request);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

}
