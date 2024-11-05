package org.example.opp_cw.controller;

import org.example.opp_cw.model.Admin;
import org.example.opp_cw.model.Customer;
import org.example.opp_cw.services.AdminService;
import org.example.opp_cw.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/admin")
    public List<Admin> sayHello() {
        return adminService.findAll();
    }

    @PostMapping("/adminsave")
    public ResponseEntity<String> saveEmployee(@RequestBody Admin customer) {
        adminService.saveAdmin(customer);
        return ResponseEntity.ok("Employee data saved successfully.");
    }

    @GetMapping("/customer")
    public List<Customer> sayHelloC() {
        return customerService.findAll();
    }

    @PostMapping("/customersave")
    public ResponseEntity<String> saveEmployeeC(@RequestBody Customer customer) {
        customerService.saveCustomer(customer);
        return ResponseEntity.ok("Employee data saved successfully.");
    }
}
