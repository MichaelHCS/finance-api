package com.apifinance.jpa.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apifinance.jpa.enums.PaymentType;
import com.apifinance.jpa.models.PaymentMethod;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {

    PaymentMethod findByTypeAndDetails(PaymentType type, String details);
    
}

