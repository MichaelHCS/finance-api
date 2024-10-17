package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.Optional; // Certifique-se de que esta importação esteja presente

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.exceptions.PaymentNotFoundException;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;

@Service
public class FraudCheckService {

    private static final Logger logger = LoggerFactory.getLogger(FraudCheckService.class);

    private final PaymentRepository paymentRepository;
    private final FraudCheckRepository fraudCheckRepository;

    public FraudCheckService(PaymentRepository paymentRepository, FraudCheckRepository fraudCheckRepository) {
        this.paymentRepository = paymentRepository;
        this.fraudCheckRepository = fraudCheckRepository;
    }

    @Transactional
public void processFraudCheck(Long paymentId, FraudCheckResult fraudStatus, FraudCheckReason checkReason) {
    Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + paymentId));

    // Verifica se o pagamento já foi processado
    if (payment.getStatus() == PaymentStatus.APPROVED || payment.getStatus() == PaymentStatus.REJECTED) {
        logger.info("Payment with ID {} has already been processed, skipping fraud check.", paymentId);
        return;
    }

    // Verifica se o fraudStatus é APPROVED ou REJECTED
    if (fraudStatus == FraudCheckResult.APPROVED || fraudStatus == FraudCheckResult.REJECTED) {
        // Verifica se já existe um registro de verificação de fraude para este pagamento
        Optional<FraudCheck> existingFraudCheck = fraudCheckRepository.findByPayment(payment);
        
        if (existingFraudCheck.isPresent()) {
            FraudCheck fraudCheck = existingFraudCheck.get();
            // Atualiza o registro existente
            fraudCheck.setFraudStatus(fraudStatus);
            fraudCheck.setCheckReason(checkReason);
            fraudCheck.setCheckedAt(ZonedDateTime.now());
            fraudCheckRepository.save(fraudCheck);

            // Atualiza o status do pagamento de acordo com o resultado da análise de fraude
            payment.setStatus(fraudStatus == FraudCheckResult.APPROVED ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);
            paymentRepository.save(payment);
            logger.info("Fraud check updated for payment ID: {}", paymentId);
        } else {
            // Se não existe um registro, cria um novo
            FraudCheck fraudCheck = new FraudCheck();
            fraudCheck.setPayment(payment);
            fraudCheck.setFraudStatus(fraudStatus);
            fraudCheck.setCheckReason(checkReason);
            fraudCheck.setCheckedAt(ZonedDateTime.now());
            fraudCheckRepository.save(fraudCheck);

            // Atualiza o status do pagamento de acordo com o resultado da análise de fraude
            payment.setStatus(fraudStatus == FraudCheckResult.APPROVED ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);
            paymentRepository.save(payment);
            logger.info("Fraud check created for payment ID: {}", paymentId);
        }
    } else {
        logger.warn("Fraud check result is neither APPROVED nor REJECTED for payment ID: {}", paymentId);
    }
    }

}