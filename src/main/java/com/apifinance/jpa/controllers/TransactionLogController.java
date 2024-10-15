package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.Optional;

import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.repositories.TransactionLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction-logs")
public class TransactionLogController {

    @Autowired  
    private TransactionLogRepository transactionLogRepository; 

    // Criar um novo TransactionLog
    @PostMapping
    public ResponseEntity<TransactionLog> createTransactionLog(@RequestBody TransactionLog transactionLog) {
        if (transactionLog == null) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
        TransactionLog createdTransactionLog = transactionLogRepository.save(transactionLog); 
        return ResponseEntity.status(201).body(createdTransactionLog); // 201 Created
    }

    // Obter todos os TransactionLogs
    @GetMapping
    public ResponseEntity<List<TransactionLog>> getAllTransactionLogs() {
        List<TransactionLog> transactionLogs = transactionLogRepository.findAll(); 
        return ResponseEntity.ok(transactionLogs); // 200 OK
    }

    // Obter TransactionLog por ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionLog> getTransactionLogById(@PathVariable Long id) {
        Optional<TransactionLog> transactionLog = transactionLogRepository.findById(id);
        if (transactionLog.isPresent()) {
            return ResponseEntity.ok(transactionLog.get()); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Atualizar TransactionLog
    @PutMapping("/{id}")
    public ResponseEntity<TransactionLog> updateTransactionLog(@PathVariable Long id, @RequestBody TransactionLog transactionLog) {
        if (!transactionLogRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        transactionLog.setId(id); // Definir ID do TransactionLog a ser atualizado
        TransactionLog updatedTransactionLog = transactionLogRepository.save(transactionLog);
        return ResponseEntity.ok(updatedTransactionLog); // 200 OK
    }

    // Deletar TransactionLog
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLog(@PathVariable Long id) {
        if (!transactionLogRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
        transactionLogRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
