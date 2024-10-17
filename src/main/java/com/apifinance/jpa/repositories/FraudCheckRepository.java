package com.apifinance.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;

import java.util.Optional;

@Repository
public interface FraudCheckRepository extends JpaRepository<FraudCheck, Long> {
    Optional<FraudCheck> findByPayment(Payment payment);
}
