package com.apifinance.jpa.services;

import java.util.List;
import java.util.Optional; // Supondo que você tenha um CustomerRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.repositories.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        return optionalCustomer.orElse(null); // Retorna null se não encontrar
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Long id, Customer customer) {
        if (customerRepository.existsById(id)) {
            customer.setId(id); // Define o ID do cliente a ser atualizado
            return customerRepository.save(customer);
        }
        return null; // Retorna null se o cliente não existir
    }

    public boolean delete(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false; // Retorna false se o cliente não existir
    }
}
