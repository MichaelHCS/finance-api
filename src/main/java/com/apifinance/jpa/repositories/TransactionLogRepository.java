package com.apifinance.jpa.repositories;

import com.apifinance.jpa.models.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    // Você pode adicionar métodos personalizados aqui, se necessário
}
