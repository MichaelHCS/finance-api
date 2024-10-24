package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.services.TransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction-logs")
public class TransactionLogController {

    @Autowired
    private TransactionLogService transactionLogService;

    @GetMapping
    public List<TransactionLog> getAllTransactionLogs() {
        return transactionLogService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionLog> getTransactionLogById(@PathVariable Long id) {
        TransactionLog transactionLog = transactionLogService.findById(id);
        return transactionLog != null ? ResponseEntity.ok(transactionLog) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public TransactionLog createTransactionLog(@RequestBody TransactionLog transactionLog) {
        return transactionLogService.save(transactionLog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionLog> updateTransactionLog(@PathVariable Long id, @RequestBody TransactionLog transactionLog) {
        transactionLog.setId(id);
        TransactionLog updatedTransactionLog = transactionLogService.save(transactionLog);
        return ResponseEntity.ok(updatedTransactionLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLog(@PathVariable Long id) {
        transactionLogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
