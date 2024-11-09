package org.example.opp_cw.services;

import org.example.opp_cw.dto.requestbody.SignUpRequest;
import org.example.opp_cw.enums.UsersType;
import org.example.opp_cw.model.Admin;
import org.example.opp_cw.model.Contact;
import org.example.opp_cw.model.Credentials;
import org.example.opp_cw.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AdminService adminService;

    public boolean signUp(UsersType usersType, SignUpRequest signUpRequest) {
        if (usersType == UsersType.VENDOR) {
            return false;
        }
        if (usersType == UsersType.ADMIN) {
           return false;
        } else {
            Credentials credentials = credentialRegister(signUpRequest);
            Contact contact = contactRegister(signUpRequest);
            Customer customer = customerSignUp(signUpRequest);
            customerService.saveCustomer(customer, credentials, contact);
        }
        return true;
    }

    private Customer customerSignUp(SignUpRequest signUpRequest) {
        Customer customer = new Customer();
        // Setting customer details
        customer.setName(signUpRequest.getName());
        customer.setSurname(signUpRequest.getSurname());
        customer.setGender(signUpRequest.getGender());
        customer.setDateOfBirth(signUpRequest.getDateOfBirth());
        customer.setNic(signUpRequest.getNic());
        customer.setAddress(signUpRequest.getAddress());
        customer.setNationality(signUpRequest.getNationality());
        return customer;
    }

    private Admin adminSignUp(SignUpRequest signUpRequest) {
        Admin admin = new Admin();
        return admin;
    }

    private Credentials credentialRegister(SignUpRequest signUpRequest) {
        Credentials credentials = new Credentials();
        // Setting credentials details
        credentials.setUserName(signUpRequest.getUserName());
        credentials.setPassword(signUpRequest.getPassword());
        return credentials;
    }

    private Contact contactRegister(SignUpRequest signUpRequest) {
        Contact contact = new Contact();
        // Setting contact details
        contact.setEmail(signUpRequest.getEmail());
        contact.setPhoneNumber(signUpRequest.getPhoneNumber());
        return contact;
    }
}
