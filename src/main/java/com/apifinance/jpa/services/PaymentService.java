package com.apifinance.jpa.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;
import com.apifinance.jpa.exceptions.PaymentNotFoundException;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final FraudCheckRepository fraudCheckRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, FraudCheckRepository fraudCheckRepository) {
        this.paymentRepository = paymentRepository;
        this.fraudCheckRepository = fraudCheckRepository;
    }

    //criar um novo pagamento
    public Payment createPayment(Payment payment) {
        payment.setStatus(PaymentStatus.PENDING); 
        return paymentRepository.save(payment);
    }

    // obter pagamento por ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
    }

    // listar todos os pagamentos
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // atualizar pagamento existente
    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment payment = getPaymentById(id); 
        payment.setAmount(paymentDetails.getAmount());
        payment.setCurrency(paymentDetails.getCurrency());
        payment.setStatus(paymentDetails.getStatus());
        payment.setPaymentMethod(paymentDetails.getPaymentMethod());
        return paymentRepository.save(payment); 
    }

    // deletar um pagamento
    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id);
        paymentRepository.delete(payment); 
    }

    // filtrar pagamentos por status
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status); 
    }

    // processar a verificação de fraude
    public void processFraudCheck(Long paymentId, FraudCheckResult fraudResult, FraudCheckReason checkReason) {
        // Atualiza o status do pagamento
        Payment payment = getPaymentById(paymentId);
        payment.setStatus(fraudResult == FraudCheckResult.APPROVED ? PaymentStatus.APPROVED : PaymentStatus.REJECTED);
        paymentRepository.save(payment); 

        // Cria uma nova entrada na tabela fraud_check
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setPaymentId(paymentId);
        fraudCheck.setFraudStatus(fraudResult);
        fraudCheck.setCheckReason(checkReason);
        fraudCheck.setCheckedAt(LocalDateTime.now());
        // fraudCheck.setRabbitmqMessageId(...);

        fraudCheckRepository.save(fraudCheck); 
    }
}
