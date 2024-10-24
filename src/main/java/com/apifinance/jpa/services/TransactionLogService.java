package com.apifinance.jpa.services;

import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.repositories.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionLogService {

    private final TransactionLogRepository transactionLogRepository;

    @Autowired
    public TransactionLogService(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    public List<TransactionLog> findAll() {
        return transactionLogRepository.findAll();
    }

    public TransactionLog findById(Long id) {
        return transactionLogRepository.findById(id).orElse(null);
    }

    public TransactionLog save(TransactionLog transactionLog) {
        return transactionLogRepository.save(transactionLog);
    }

    public void deleteById(Long id) {
        transactionLogRepository.deleteById(id);
    }
}
