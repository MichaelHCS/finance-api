package com.apifinance.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apifinance.jpa.models.FraudCheck;

public interface FraudCheckRepository extends JpaRepository<FraudCheck, Long> {
    
}
