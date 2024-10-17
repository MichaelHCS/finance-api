package com.apifinance.jpa.services;

import java.time.ZonedDateTime;

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

        // Atualiza o status do pagamento de acordo com o resultado da análise de fraude
        if (fraudStatus == FraudCheckResult.APPROVED) {
            payment.setStatus(PaymentStatus.APPROVED);
        } else if (fraudStatus == FraudCheckResult.REJECTED) {
            payment.setStatus(PaymentStatus.REJECTED);
        }
        paymentRepository.save(payment);

        // Crie um novo registro na tabela fraud_check
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setPayment(payment);
        fraudCheck.setFraudStatus(fraudStatus);
        fraudCheck.setCheckReason(checkReason);
        fraudCheck.setCheckedAt(ZonedDateTime.now());
        fraudCheckRepository.save(fraudCheck);

        logger.info("Fraud check processed for payment ID: {}", paymentId);
    }
}
