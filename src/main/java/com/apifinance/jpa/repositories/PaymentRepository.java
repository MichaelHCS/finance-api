package com.apifinance.jpa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.enums.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStatus(PaymentStatus status); // Método para encontrar pagamentos por status
}
