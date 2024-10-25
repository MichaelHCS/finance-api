package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.FraudCheckStatus;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;

@Service
public class FraudCheckService {

    @Autowired
    private FraudCheckRepository fraudCheckRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<FraudCheck> findAll() {
        return fraudCheckRepository.findAll();
    }

    public FraudCheck findById(Long id) {
        return fraudCheckRepository.findById(id).orElse(null);
    }

    public FraudCheck save(FraudCheck fraudCheck) {
        return fraudCheckRepository.save(fraudCheck);
    }

    public void deleteById(Long id) {
        fraudCheckRepository.deleteById(id);
    }

    public void analyzeFraud(Long id, FraudCheck fraudCheck) {
        FraudCheck existingFraudCheck = findById(id);
        if (existingFraudCheck != null) {
            Payment payment = existingFraudCheck.getPayment();
            boolean isFraudulent = fraudCheck.getFraudStatus() == FraudCheckStatus.REJECTED;

            // Atualiza o status do pagamento
            payment.setPaymentStatus(isFraudulent ? PaymentStatus.REJECTED : PaymentStatus.APPROVED);
            paymentRepository.save(payment);

            // Atualiza os detalhes de fraude
            existingFraudCheck.setCheckedAt(ZonedDateTime.now());
            existingFraudCheck.setFraudStatus(isFraudulent ? FraudCheckStatus.REJECTED : FraudCheckStatus.APPROVED);
            existingFraudCheck.setFraudReason(fraudCheck.getFraudReason());

            fraudCheckRepository.save(existingFraudCheck);
        }
    }
}
