package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.dtos.ErrorResponse;
import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.services.CustomerService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCustomerById(@PathVariable UUID id) {
        logger.info("Buscando cliente com ID: {}", id);
        return customerService.findCustomerById(id)
                .<ResponseEntity<Object>>map(customer -> {
                    logger.info("Cliente encontrado: {}", customer);
                    return ResponseEntity.status(HttpStatus.CREATED).body(customer); // Alterado para 201 Created
                })
                .orElseGet(() -> {
                    logger.warn("Cliente com ID: {} não encontrado", id);
                    ErrorResponse errorResponse = new ErrorResponse("Cliente não encontrado para o ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable UUID id, @RequestBody Customer updatedCustomer) {
        logger.info("Recebendo requisição para atualizar cliente com ID: {}", id);

        try {
            customerService.updateCustomer(id, updatedCustomer);
            return ResponseEntity.ok("Cliente atualizado com sucesso.");
        } catch (IllegalArgumentException e) {
            logger.error("Erro ao atualizar cliente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllCustomers() {
        logger.info("Buscando todos os clientes");
        List<Customer> customers = customerService.findAllCustomers();

        if (customers.isEmpty()) {
            //logger.warn("Nenhum cliente encontrado");
            ErrorResponse errorResponse = new ErrorResponse("Nenhum cliente encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // ou HttpStatus.NO_CONTENT
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(customers); // Retorna 201 Created
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID id) {
        logger.info("Recebendo requisição para deletar cliente com ID: {}", id);
        try {
            boolean deleted = customerService.deleteCustomerById(id);

            if (deleted) {
                logger.info("Cliente com ID: {} deletado com sucesso", id);
                return ResponseEntity.status(HttpStatus.CREATED).body("Cliente deletado com sucesso."); // Mudança para 201 Created
            } else {
                logger.warn("Cliente com ID: {} não encontrado", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
            }
        } catch (Exception e) {
            logger.error("Erro ao deletar cliente com ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar cliente.");
        }
    }

}
