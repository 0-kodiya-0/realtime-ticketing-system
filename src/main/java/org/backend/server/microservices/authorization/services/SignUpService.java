package org.backend.server.microservices.authorization.services;

import org.backend.server.microservices.authorization.dto.SignUpRequest;
import org.backend.server.microservices.authorization.models.Customer;
import org.backend.server.microservices.authorization.models.Vendor;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    private CustomerService customerService;
    private VendorService vendorService;

    public SignUpService(CustomerService customerService, VendorService vendorService) {
        this.customerService = customerService;
        this.vendorService = vendorService;
    }

    public void signupCustomer(SignUpRequest signUpRequest) throws NullPointerException{
        if (signUpRequest == null || signUpRequest.getPerson() == null || signUpRequest.getCredentials() == null || signUpRequest.getContact() == null) {
            throw new NullPointerException("SignUpRequest must contain person, credentials and contact");
        }
        Customer customer = new Customer();
        customer.setName(signUpRequest.getPerson().getName());
        customer.setSurname(signUpRequest.getPerson().getSurname());
        customer.setGender(signUpRequest.getPerson().getGender());
        customer.setDateOfBirth(signUpRequest.getPerson().getDateOfBirth());
        customer.setAge(signUpRequest.getPerson().getAge());
        customer.setNic(signUpRequest.getPerson().getNic());
        customer.setAddress(signUpRequest.getPerson().getAddress());
        customer.setContact(signUpRequest.getPerson().getContact());
        customer.setVisible(true);
        customer.setSystemAuthorized(true);
        customer.setCredentials(signUpRequest.getCredentials());
        customerService.save(customer);
    }

    public void signupVendor(SignUpRequest signUpRequest) throws NullPointerException {
        if (signUpRequest == null || signUpRequest.getPerson() == null || signUpRequest.getCredentials() == null || signUpRequest.getContact() == null) {
            throw new NullPointerException("SignUpRequest must contain person, credentials and contact");
        }
        Vendor vendor = new Vendor();
        vendor.setName(signUpRequest.getPerson().getName());
        vendor.setSurname(signUpRequest.getPerson().getSurname());
        vendor.setGender(signUpRequest.getPerson().getGender());
        vendor.setDateOfBirth(signUpRequest.getPerson().getDateOfBirth());
        vendor.setAge(signUpRequest.getPerson().getAge());
        vendor.setNic(signUpRequest.getPerson().getNic());
        vendor.setAddress(signUpRequest.getPerson().getAddress());
        vendor.setContact(signUpRequest.getPerson().getContact());
        vendor.setVisible(true);
        vendor.setSystemAuthorized(true);
        vendor.setCredentials(signUpRequest.getCredentials());
        vendorService.save(vendor);
    }
}
