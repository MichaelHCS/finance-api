package com.apifinance.jpa.services;

import java.util.List;
import java.util.Optional; // Supondo que você tenha um TransactionLogRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.repositories.TransactionLogRepository;

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
        Optional<TransactionLog> optionalLog = transactionLogRepository.findById(id);
        return optionalLog.orElse(null); // Retorna null se não encontrar
    }

    public TransactionLog save(TransactionLog transactionLog) {
        return transactionLogRepository.save(transactionLog);
    }

    public TransactionLog update(Long id, TransactionLog transactionLog) {
        if (transactionLogRepository.existsById(id)) {
            transactionLog.setId(id); // Define o ID do log a ser atualizado
            return transactionLogRepository.save(transactionLog);
        }
        return null; // Retorna null se o log não existir
    }

    public boolean delete(Long id) {
        if (transactionLogRepository.existsById(id)) {
            transactionLogRepository.deleteById(id);
            return true;
        }
        return false; // Retorna false se o log não existir
    }
}
