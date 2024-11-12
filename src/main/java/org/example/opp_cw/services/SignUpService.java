package org.example.opp_cw.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.opp_cw.dto.requestbody.AdminSignUpRequest;
import org.example.opp_cw.dto.requestbody.CustomerSignUpRequest;
import org.example.opp_cw.dto.responsebody.ApiResponse;
import org.example.opp_cw.enums.UsersType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdminService adminService;

    public ApiResponse signUp(CustomerSignUpRequest customerSignUpRequest, HttpServletRequest request) {
        customerService.saveCustomer(customerSignUpRequest.getCustomer(), customerSignUpRequest.getCredentials(), customerSignUpRequest.getContact());
        return new ApiResponse(HttpStatus.OK, "signup success full", request);
    }

    public ApiResponse signUp(AdminSignUpRequest adminSignUpRequest, HttpServletRequest request) {
        adminService.saveAdmin(adminSignUpRequest.getAdmin(), adminSignUpRequest.getCredentials(), adminSignUpRequest.getContact());
        return new ApiResponse(HttpStatus.OK, "signup success full", request);
    }
}
