package com.apifinance.jpa.repositories;

import com.apifinance.jpa.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    // Você pode adicionar métodos personalizados aqui, se necessário
}

