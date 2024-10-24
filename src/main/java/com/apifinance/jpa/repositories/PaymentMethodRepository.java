package com.apifinance.jpa.repositories;

import com.apifinance.jpa.enums.PaymentMethodDetails;
import com.apifinance.jpa.enums.PaymentType;
import com.apifinance.jpa.models.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    PaymentMethod findByTypeAndDetails(PaymentType type, PaymentMethodDetails details);
    // Você pode adicionar métodos personalizados aqui, se necessário
}

