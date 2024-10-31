package com.apifinance.jpa.services;

import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.repositories.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void validateCustomer(Customer customer) throws IllegalArgumentException {
        logger.info("Validando cliente: {}", customer.getName());
        
        if (customer.getName() == null || customer.getName().isEmpty() || customer.getName().length() > 100) {
            logger.error("Nome inválido: {}", customer.getName());
            throw new IllegalArgumentException("O nome deve ser fornecido e ter no máximo 100 caracteres.");
        }

        if (customer.getEmail() == null || !isValidEmail(customer.getEmail())) {
            logger.error("Email inválido: {}", customer.getEmail());
            throw new IllegalArgumentException("Email inválido.");
        }

        if (customer.getPhoneNumber() == null || !isValidPhoneNumber(customer.getPhoneNumber())) {
            logger.error("Número de telefone inválido: {}", customer.getPhoneNumber());
            throw new IllegalArgumentException("Número de telefone inválido. Deve ter entre 10 e 15 caracteres.");
        }

        logger.info("Cliente validado com sucesso: {}", customer.getName());
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"; 
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() >= 10 && phoneNumber.length() <= 15; 
    }

    public Customer saveCustomer(Customer customer) {
        logger.info("Salvando cliente: {}", customer.getName());
        return customerRepository.save(customer);
    }
}
