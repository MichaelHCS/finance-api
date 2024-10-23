package com.apifinance.jpa.repositories;

import com.apifinance.jpa.models.FraudCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudCheckRepository extends JpaRepository<FraudCheck, Long> {
    // Você pode adicionar métodos personalizados aqui, se necessário
}
