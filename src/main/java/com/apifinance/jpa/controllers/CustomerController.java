package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.services.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        logger.info("Recebendo requisição para criar cliente: {}", customer.getName());
        try {
            customerService.validateCustomer(customer);
            customerService.saveCustomer(customer);
            logger.info("Cliente criado com sucesso: {}", customer.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente criado com sucesso.");
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao criar cliente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
