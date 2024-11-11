package org.example.opp_cw.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.opp_cw.dto.requestbody.SignUpRequest;
import org.example.opp_cw.dto.responsebody.ApiResponse;
import org.example.opp_cw.enums.UsersType;
import org.example.opp_cw.model.Admin;
import org.example.opp_cw.model.Contact;
import org.example.opp_cw.model.Credentials;
import org.example.opp_cw.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdminService adminService;

    public ApiResponse signUp(UsersType usersType, SignUpRequest signUpRequest, HttpServletRequest request) {
        if (usersType == UsersType.VENDOR) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
        }
        if (usersType == UsersType.ADMIN) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", request);
        } else {
            customerService.saveCustomer(signUpRequest.getCustomer(), signUpRequest.getCredentials(), signUpRequest.getContact());
        }
        return new ApiResponse(HttpStatus.OK, "signup success full", request);
    }
}
