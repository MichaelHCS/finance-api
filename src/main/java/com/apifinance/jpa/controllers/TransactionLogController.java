package com.apifinance.jpa.controllers;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.TransactionLogDetails;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.services.TransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction-logs")
public class TransactionLogController {

    @Autowired
    private TransactionLogService transactionLogService;

    // Operação de leitura para buscar todos os registros de logs
    @GetMapping
    public List<TransactionLog> getAllTransactionLogs() {
        return transactionLogService.findAll();
    }

    // Operação de leitura para buscar um registro específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionLog> getTransactionLogById(@PathVariable UUID id) {
        TransactionLog transactionLog = transactionLogService.findById(id);
        return transactionLog != null ? ResponseEntity.ok(transactionLog) : ResponseEntity.notFound().build();
    }

    // Operação de criação para registrar um log de transação
    @PostMapping
    public ResponseEntity<Void> createTransactionLog(
            @RequestBody Payment payment,
            @RequestParam TransactionLogDetails action,
            @RequestParam(required = false) FraudCheckReason reason) {
        
        // Cria o log com base no tipo de ação e, se aplicável, na razão de fraude
        if (action == TransactionLogDetails.PAYMENT_CREATED) {
            transactionLogService.logPaymentCreated(payment);
        } else if (action == TransactionLogDetails.FRAUD_DETECTED) {
            transactionLogService.logFraudDetected(payment, reason);
        }

        return ResponseEntity.status(201).build();
    }
}
