package org.example.opp_cw.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.opp_cw.dto.requestbody.LoginRequest;
import org.example.opp_cw.dto.responsebody.ApiResponse;
import org.example.opp_cw.model.Credentials;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {


    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AdminService adminService;
    private final CustomerService customerService;

    public LoginService(BCryptPasswordEncoder bCryptPasswordEncoder, AdminService adminService, CustomerService customerService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.adminService = adminService;
        this.customerService = customerService;
    }

    public ApiResponse loginCustomer(LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Credentials credentials = customerService.isCustomer(loginRequest.getCredentials().getUserName());
            if (credentials == null) {
                return new ApiResponse(HttpStatus.NOT_FOUND, "credentials not found", request);
            }
            if (!customerService.isCustomer(credentials.get_id())) {
                return new ApiResponse(HttpStatus.NOT_FOUND, "customer not found", request);
            }
            if (!customerService.isCustomerVerified(credentials.get_id())) {
                return new ApiResponse(HttpStatus.FORBIDDEN, "customer found, not verified", request);
            }
            if (!bCryptPasswordEncoder.matches(loginRequest.getCredentials().getPassword(), credentials.getPassword())) {
                return new ApiResponse(HttpStatus.UNAUTHORIZED, "password invalid", request);
            }
            return new ApiResponse(HttpStatus.OK, "login success full", request);
        } catch (IncorrectResultSizeDataAccessException e) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal db server error", request);
        }
    }

    public ApiResponse loginAdmin(LoginRequest loginRequest, HttpServletRequest request) {
        try {
            Credentials credentials = adminService.isAdmin(loginRequest.getCredentials().getUserName());
            if (credentials == null) {
                return new ApiResponse(HttpStatus.NOT_FOUND, "credentials not found", request);
            }
            if (!adminService.isAdmin(credentials.get_id())) {
                return new ApiResponse(HttpStatus.NOT_FOUND, "admin not found", request);
            }
            if (!adminService.isAdminVerified(credentials.get_id())) {
                return new ApiResponse(HttpStatus.FORBIDDEN, "admin found, not verified", request);
            }
            if (!bCryptPasswordEncoder.matches(loginRequest.getCredentials().getPassword(), credentials.getPassword())) {
                return new ApiResponse(HttpStatus.UNAUTHORIZED, "password invalid", request);
            }
            return new ApiResponse(HttpStatus.OK, "login success full", request);
        } catch (IncorrectResultSizeDataAccessException e) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal db server error", request);
        }
    }
}
