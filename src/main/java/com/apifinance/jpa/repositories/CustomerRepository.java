package com.apifinance.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apifinance.jpa.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Você pode adicionar consultas personalizadas aqui, se necessário
}
