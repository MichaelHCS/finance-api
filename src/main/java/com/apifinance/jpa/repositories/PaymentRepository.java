package com.apifinance.jpa.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apifinance.jpa.models.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
