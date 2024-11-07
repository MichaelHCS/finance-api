package com.apifinance.jpa.services;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.TransactionLogDetails;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.repositories.TransactionLogRepository;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TransactionLogService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionLogService.class);

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    public void logTransaction(Payment payment, TransactionLogDetails action, FraudCheckReason reason) {

        if (payment == null) {
            logger.error("Não é possível registrar o log. O objeto Payment é nulo.");
            return;
        }

        String details = action.getDetails(payment, reason);

        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setPayment(payment);
        transactionLog.setAction(action);
        transactionLog.setTimestamp(ZonedDateTime.now());
        transactionLog.setDetails(details);

        transactionLogRepository.save(transactionLog);
        logger.info("Log registrado: {}", details);
    }

    public void logPaymentCreated(Payment payment) {
        logTransaction(payment, TransactionLogDetails.PAYMENT_CREATED, null);
    }

    public void logFraudDetected(Payment payment, FraudCheckReason reason) {
        logTransaction(payment, TransactionLogDetails.FRAUD_DETECTED, reason);
    }

    public List<TransactionLog> findAll() {
        return transactionLogRepository.findAll();
    }

    public TransactionLog findById(UUID id) {
        Optional<TransactionLog> transactionLog = transactionLogRepository.findById(id);
        return transactionLog.orElse(null);
    }

}
