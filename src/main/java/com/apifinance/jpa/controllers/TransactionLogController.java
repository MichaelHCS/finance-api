package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.services.TransactionLogService; // Supondo que você tenha um TransactionLogService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction-logs")
public class TransactionLogController {

    private final TransactionLogService transactionLogService;

    @Autowired
    public TransactionLogController(TransactionLogService transactionLogService) {
        this.transactionLogService = transactionLogService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionLog>> getAllLogs() {
        List<TransactionLog> logs = transactionLogService.findAll();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionLog> getLogById(@PathVariable Long id) {
        TransactionLog log = transactionLogService.findById(id);
        return log != null ? ResponseEntity.ok(log) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TransactionLog> createLog(@RequestBody TransactionLog transactionLog) {
        TransactionLog createdLog = transactionLogService.save(transactionLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionLog> updateLog(@PathVariable Long id, @RequestBody TransactionLog transactionLog) {
        TransactionLog updatedLog = transactionLogService.update(id, transactionLog);
        return updatedLog != null ? ResponseEntity.ok(updatedLog) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        return transactionLogService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
