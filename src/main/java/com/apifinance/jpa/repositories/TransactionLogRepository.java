package com.apifinance.jpa.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apifinance.jpa.models.TransactionLog;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {
    
}
