package com.apifinance.jpa.repositories;

import com.apifinance.jpa.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Você pode adicionar métodos personalizados aqui, se necessário
}
