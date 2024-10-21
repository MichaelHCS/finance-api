package com.apifinance.jpa.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;
import com.apifinance.jpa.enums.PaymentStatus;

@Service
public class FraudCheckService {

    private final FraudCheckRepository fraudCheckRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public FraudCheckService(FraudCheckRepository fraudCheckRepository, PaymentRepository paymentRepository) {
        this.fraudCheckRepository = fraudCheckRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<FraudCheck> findAll() {
        return fraudCheckRepository.findAll();
    }

    public FraudCheck findById(Long id) {
        Optional<FraudCheck> optionalFraudCheck = fraudCheckRepository.findById(id);
        return optionalFraudCheck.orElse(null); // Retorna null se não encontrar
    }

    public FraudCheck save(FraudCheck fraudCheck) {
        return fraudCheckRepository.save(fraudCheck);
    }

    public FraudCheck update(Long id, FraudCheck fraudCheck) {
        if (fraudCheckRepository.existsById(id)) {
            fraudCheck.setId(id); // Define o ID da verificação de fraude a ser atualizada
            return fraudCheckRepository.save(fraudCheck);
        }
        return null; // Retorna null se a verificação de fraude não existir
    }

    public boolean delete(Long id) {
        if (fraudCheckRepository.existsById(id)) {
            fraudCheckRepository.deleteById(id);
            return true;
        }
        return false; // Retorna false se a verificação de fraude não existir
    }

    public void analyzeFraud(Long paymentId, boolean isFraud, FraudCheckReason checkReason) {
        // Encontrar o pagamento correspondente
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();

            // Criar uma nova análise de fraude
            FraudCheck fraudCheck = new FraudCheck();
            fraudCheck.setPaymentId(paymentId);
            fraudCheck.setFraudStatus(isFraud ? FraudCheckStatus.REJECTED : FraudCheckStatus.APPROVED);
            fraudCheck.setCheckReason(checkReason);
            fraudCheckRepository.save(fraudCheck); // Salva a análise de fraude

            // Atualizar o status do pagamento
            payment.setPaymentStatus(isFraud ? PaymentStatus.REJECTED : PaymentStatus.APPROVED); // Atualiza o status do pagamento
            paymentRepository.save(payment); // Salva as atualizações do pagamento
        }
    }
}
