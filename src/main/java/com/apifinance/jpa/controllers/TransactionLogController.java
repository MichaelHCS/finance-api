package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.TransactionLogDetails;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.services.TransactionLogService;

@RestController
@RequestMapping("/transaction-logs")
public class TransactionLogController {

    @Autowired
    private TransactionLogService transactionLogService;

    @GetMapping
    public ResponseEntity<List<TransactionLog>> getAllTransactionLogs() {
        try {
            List<TransactionLog> transactionLogs = transactionLogService.findAll();
            return ResponseEntity.ok(transactionLogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTransactionLogById(@PathVariable UUID id) {
        try {
            TransactionLog transactionLog = transactionLogService.findById(id);
            if (transactionLog != null) {
                return ResponseEntity.ok(transactionLog);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Log de transação não encontrado com ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao recuperar o log de transação: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createTransactionLog(
            @RequestBody Payment payment,
            @RequestParam TransactionLogDetails action,
            @RequestParam(required = false) FraudCheckReason reason) {
        try {
            if (action == TransactionLogDetails.PAYMENT_CREATED) {
                transactionLogService.logPaymentCreated(payment);
            } else if (action == TransactionLogDetails.FRAUD_DETECTED) {
                transactionLogService.logFraudDetected(payment, reason);
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Log de transação criado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar log de transação: " + e.getMessage());
        }
    }
}
