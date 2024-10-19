package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.Map;

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
import com.apifinance.jpa.models.RabbitMQMessage;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMQMessageRepository;
import com.apifinance.jpa.requests.FraudCheckRequest; // Certifique-se de importar a classe correta

@Service
public class FraudCheckService {

    private static final Logger logger = LoggerFactory.getLogger(FraudCheckService.class);

    private final PaymentRepository paymentRepository;
    private final FraudCheckRepository fraudCheckRepository;
    private final RabbitMQMessageRepository rabbitMQMessageRepository;

    public FraudCheckService(PaymentRepository paymentRepository, FraudCheckRepository fraudCheckRepository, RabbitMQMessageRepository rabbitMQMessageRepository) {
        this.paymentRepository = paymentRepository;
        this.fraudCheckRepository = fraudCheckRepository;
        this.rabbitMQMessageRepository = rabbitMQMessageRepository;
    }

    @Transactional
    public FraudCheck createFraudCheck(FraudCheckRequest request) {
        // Busca o pagamento correspondente ao ID fornecido
        Payment payment = paymentRepository.findById(request.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + request.getPaymentId()));

        RabbitMQMessage rabbitmqMessage = rabbitMQMessageRepository.findById(request.getRabbitmqMessage())
                .orElseThrow(() -> new RuntimeException("RabbitMQ Message not found"));

        // Cria uma nova verificação de fraude
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setPayment(payment);
        fraudCheck.setFraudStatus(request.getFraudStatus());
        fraudCheck.setCheckReason(request.getCheckReason());
        fraudCheck.setRabbitmqMessage(rabbitmqMessage);
        fraudCheck.setCheckedAt(ZonedDateTime.now());

        // Atualiza o campo fraud_check_id no pagamento
        payment.setFraudCheck(fraudCheck);

        // Atualiza o status do pagamento
        updatePaymentStatus(payment, request.getFraudStatus(), request.getCheckReason());

        logger.info("Creating fraud check for payment ID: {}", request.getPaymentId());

        return fraudCheckRepository.save(fraudCheck);
    }

    @Transactional
    public FraudCheck processFraudCheck(Long paymentId, FraudCheckResult fraudStatus, FraudCheckReason checkReason) {
        // Busca o pagamento correspondente ao ID fornecido
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + paymentId));

        // Verifica se já existe um registro de verificação de fraude para este pagamento
        FraudCheck fraudCheck = fraudCheckRepository.findByPayment(payment)
            .orElse(new FraudCheck());
            
        fraudCheck.setPayment(payment);
        fraudCheck.setFraudStatus(fraudStatus);
        fraudCheck.setCheckReason(checkReason);
        fraudCheck.setCheckedAt(ZonedDateTime.now());

        // Atualiza o status do pagamento
        updatePaymentStatus(payment, fraudStatus, checkReason);

        logger.info("Fraud check processed for payment ID: {}. Fraud status: {}", paymentId, fraudStatus);
        
        return fraudCheckRepository.save(fraudCheck);
    }

    @Transactional
    public FraudCheck updateFraudCheck(Long fraudCheckId, Long paymentId, FraudCheckResult fraudStatus, FraudCheckReason checkReason) {
        // Busca a verificação de fraude correspondente
        FraudCheck fraudCheck = fraudCheckRepository.findById(fraudCheckId)
                .orElseThrow(() -> new PaymentNotFoundException("FraudCheck not found with id " + fraudCheckId));

        // Busca o pagamento correspondente ao ID fornecido
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + paymentId));

        fraudCheck.setPayment(payment);
        fraudCheck.setFraudStatus(fraudStatus);
        fraudCheck.setCheckReason(checkReason);
        fraudCheck.setCheckedAt(ZonedDateTime.now());

        // Atualiza o status do pagamento
        updatePaymentStatus(payment, fraudStatus, checkReason);

        logger.info("FraudCheck updated for ID: {}. New fraud status: {}", fraudCheckId, fraudStatus);

        return fraudCheckRepository.save(fraudCheck);
    }

    // Método auxiliar para atualizar o status do pagamento
    private void updatePaymentStatus(Payment payment, FraudCheckResult fraudStatus, FraudCheckReason checkReason) {
        Map<FraudCheckResult, PaymentStatus> statusMap = Map.of(
            FraudCheckResult.APPROVED, PaymentStatus.APPROVED,
            FraudCheckResult.REJECTED, PaymentStatus.REJECTED
        );
    
        payment.setStatus(statusMap.getOrDefault(fraudStatus, PaymentStatus.PENDING));
    
        // Armazena o motivo como uma String
        if (fraudStatus == FraudCheckResult.APPROVED || fraudStatus == FraudCheckResult.REJECTED) {
            payment.setCheckReason(checkReason.getDescription());
        } else {
            payment.setCheckReason(null); // Ou algum valor padrão
        }
    
        paymentRepository.save(payment); // Salva as alterações no pagamento
    }
}
