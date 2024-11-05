package com.apifinance.jpa.controllers;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.services.TransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<TransactionLog> getTransactionLogById(@PathVariable UUID id) {
        TransactionLog transactionLog = transactionLogService.findById(id);
        return transactionLog != null ? ResponseEntity.ok(transactionLog) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TransactionLog> createTransactionLog(@RequestBody TransactionLog transactionLog) {
        TransactionLog savedTransactionLog = transactionLogService.save(transactionLog);
        return ResponseEntity.created(URI.create("/transaction-logs/" + savedTransactionLog.getId())).body(savedTransactionLog);
    }

     @PutMapping("/{id}")
    public ResponseEntity<TransactionLog> updateTransactionLog(@PathVariable UUID id, @RequestBody TransactionLog transactionLog) {
        transactionLog.setId(id);
        TransactionLog updatedTransactionLog = transactionLogService.save(transactionLog);
        return ResponseEntity.ok(updatedTransactionLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLog(@PathVariable UUID id) {
        transactionLogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payment-created")
    public ResponseEntity<Void> logPaymentCreated(@RequestBody Payment payment) {
        transactionLogService.logPaymentCreated(payment);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/fraud-detected")
    public ResponseEntity<Void> logFraudDetected(@RequestBody Payment payment, @RequestParam FraudCheckReason reason) {
        transactionLogService.logFraudDetected(payment, reason);
        return ResponseEntity.status(201).build();
    }
}
