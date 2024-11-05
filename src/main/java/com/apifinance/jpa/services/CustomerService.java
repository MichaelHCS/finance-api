package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.repositories.CustomerRepository;

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

    public Optional<Customer> findCustomerById(UUID id) {
        logger.info("Buscando cliente com ID: {}", id);
        return customerRepository.findById(id);
    }

    public List<Customer> findAllCustomers() {
        logger.info("Buscando todos os clientes");
        return customerRepository.findAll();
    }       

    public Customer updateCustomer(UUID id, Customer updatedCustomer) throws IllegalArgumentException {
        // Valida as informações do cliente antes de prosseguir com a atualização
        validateCustomer(updatedCustomer);
    
        return customerRepository.findById(id).map(existingCustomer -> {
            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
            
            // Atualiza o campo createdAt com o valor do updatedCustomer ou usa ZonedDateTime.now() se for necessário
            existingCustomer.setCreatedAt(updatedCustomer.getCreatedAt() != null ? updatedCustomer.getCreatedAt() : ZonedDateTime.now());
            
            return customerRepository.save(existingCustomer); // Salva as alterações
        }).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado")); // Lança exceção se não encontrar o cliente
    }
    

    public boolean deleteCustomerById(UUID id) {
        logger.info("Buscando e deletando cliente com ID: {}", id);
        return customerRepository.findById(id).map(customer -> {
            customerRepository.delete(customer);
            return true;
        }).orElse(false);
    }
    
}
