package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.repositories.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired  
    private CustomerRepository customerRepository; 

    // Criar um novo cliente
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        if (customer == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        Customer createdCustomer = customerRepository.save(customer); 
        return ResponseEntity.status(201).body(createdCustomer); 
    }

    // Obter todos os clientes
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll(); 
        return ResponseEntity.ok(customers); // 200 OK
    }

    // Obter cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get()); 
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Atualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); 
        }
        customer.setId(id); // Definir ID do cliente a ser atualizado
        Customer updatedCustomer = customerRepository.save(customer);
        return ResponseEntity.ok(updatedCustomer); // 200 OK
    }

    // Deletar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
