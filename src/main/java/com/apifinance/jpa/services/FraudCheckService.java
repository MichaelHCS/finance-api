package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.FraudCheckReason;
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

    public void analyzeFraud(Long paymentId, FraudCheckStatus fraudStatus, FraudCheckReason fraudReason) {
        // Busca o pagamento correspondente
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado para o ID: " + paymentId));

        // Cria um novo registro de análise de fraude
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setPayment(payment);
        fraudCheck.setFraudStatus(fraudStatus);
        fraudCheck.setFraudReason(fraudReason);
        fraudCheck.setCheckedAt(ZonedDateTime.now()); // Define a data/hora da análise

        // Salva o registro de análise de fraude na tabela fraud_check
        fraudCheckRepository.save(fraudCheck);

        // Atualiza o status do pagamento com base no resultado da análise de fraude
        // Atualiza o status do pagamento com base no resultado da análise de fraude
        if (fraudStatus == FraudCheckStatus.APPROVED) { // Usando a variável fraudStatus
            payment.setStatus(PaymentStatus.APPROVED); // Define o status como "Aprovado"
        } else if (fraudStatus == FraudCheckStatus.REJECTED) { // Usando a variável fraudStatus
            payment.setStatus(PaymentStatus.REJECTED); // Define o status como "Rejeitado"
        }
        

        // Salva a atualização do pagamento na tabela payment
        paymentRepository.save(payment);

    }
     

}