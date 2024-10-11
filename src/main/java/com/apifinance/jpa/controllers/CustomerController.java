package com.apifinance.jpa.controllers;

import java.util.List;

import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.repositories.CustomerRepository; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository; 

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerRepository.save(customer); 
        return ResponseEntity.status(201).body(createdCustomer);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll(); 
        return ResponseEntity.ok(customers);
    }
}
