package org.example.opp_cw.services;

import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.example.opp_cw.dto.requestbody.AdminSignUpRequest;
import org.example.opp_cw.dto.requestbody.CustomerSignUpRequest;
import org.example.opp_cw.dto.responsebody.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SignUpService {

    private final CustomerService customerService;
    private final AdminService adminService;
    private final Map<String, ObjectId> signTokenCache;

    public SignUpService(CustomerService customerService, AdminService adminService) {
        this.customerService = customerService;
        this.adminService = adminService;
        signTokenCache = new HashMap<>();
    }

    public ApiResponse signUp(String signupTokenId, CustomerSignUpRequest customerSignUpRequest, HttpServletRequest request) {
        System.out.println(signupTokenId);
        if (customerSignUpRequest.getCustomer() == null && customerSignUpRequest.getCredentials() == null && customerSignUpRequest.getContact() == null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "missing body data", request);
        }
        if (customerSignUpRequest.getCustomer() != null) {
            ObjectId objectId = customerService.saveCustomer(customerSignUpRequest.getCustomer());
            signTokenCache.put(signupTokenId, objectId);
        }
        ObjectId generatedObjectId = signTokenCache.get(signupTokenId);
        if (generatedObjectId == null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "customer details needed first", request);
        }
        if (customerSignUpRequest.getCredentials() != null) {
            customerService.saveCustomerCredentials(generatedObjectId, customerSignUpRequest.getCredentials());
        }
        if (customerSignUpRequest.getContact() != null) {
            customerService.saveCustomerContact(generatedObjectId, customerSignUpRequest.getContact());
        }
        return new ApiResponse(HttpStatus.OK, "signup success full", request);
    }

    public ApiResponse signUp(String signupTokenId, AdminSignUpRequest adminSignUpRequest, HttpServletRequest request) {
        if (adminSignUpRequest.getAdmin() == null && adminSignUpRequest.getCredentials() == null && adminSignUpRequest.getContact() == null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "missing body data", request);
        }
        if (adminSignUpRequest.getAdmin() != null) {
            ObjectId objectId = adminService.saveAdmin(adminSignUpRequest.getAdmin());
            signTokenCache.put(signupTokenId, objectId);
        }
        ObjectId generatedObjectId = signTokenCache.get(signupTokenId);
        if (generatedObjectId == null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "admin details needed first", request);
        }
        if (adminSignUpRequest.getCredentials() != null) {
            adminService.saveAdminCredentials(generatedObjectId, adminSignUpRequest.getCredentials());
        }
        if (adminSignUpRequest.getContact() != null) {
            adminService.saveAdminContact(generatedObjectId, adminSignUpRequest.getContact());
        }
        return new ApiResponse(HttpStatus.OK, "signup success full", request);
    }

    public ApiResponse verifySignUpCustomer(String signupTokenId, HttpServletRequest request) {
        System.out.println(signupTokenId);
        ObjectId generatedObjectId = signTokenCache.get(signupTokenId);
        if (generatedObjectId == null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "first add customer details", request);
        }
        if (customerService.isCustomer(generatedObjectId) && customerService.verifyCustomer(generatedObjectId)) {
            signTokenCache.remove(signupTokenId);
            return new ApiResponse(HttpStatus.OK, "signup success full", request);
        } else {
            return new ApiResponse(HttpStatus.NOT_FOUND, "signup not full", request);
        }
    }

    public ApiResponse verifySignUpAdmin(String signupTokenId, HttpServletRequest request) {
        ObjectId generatedObjectId = signTokenCache.get(signupTokenId);
        if (generatedObjectId == null) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "first add admin details", request);
        }
        if (adminService.isAdmin(generatedObjectId) && adminService.verifyAdmin(generatedObjectId)) {
            signTokenCache.remove(signupTokenId);
            return new ApiResponse(HttpStatus.OK, "signup success full", request);
        } else {
            return new ApiResponse(HttpStatus.NOT_FOUND, "signup not full", request);
        }
    }
}
