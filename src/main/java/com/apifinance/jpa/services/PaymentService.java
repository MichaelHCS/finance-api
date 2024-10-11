package com.apifinance.jpa.services;

import java.util.List;
import java.time.ZonedDateTime; // Importação necessária

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;
import com.apifinance.jpa.enums.MessageStatus;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.exceptions.PaymentNotFoundException;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.RabbitMQMessage; 
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final FraudCheckRepository fraudCheckRepository;
    private final RabbitTemplate rabbitTemplate; 

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, FraudCheckRepository fraudCheckRepository, RabbitTemplate rabbitTemplate) {
        this.paymentRepository = paymentRepository;
        this.fraudCheckRepository = fraudCheckRepository;
        this.rabbitTemplate = rabbitTemplate; 
    }

    // Criar um novo pagamento
    @Transactional
    public Payment createPayment(Payment payment) {
        payment.setStatus(PaymentStatus.PENDING); 
        Payment savedPayment = paymentRepository.save(payment);

        // Criar mensagem para RabbitMQ
        sendRabbitMQMessage(savedPayment.getId(), "Pagamento criado: ");

        return savedPayment;
    }

    // Método auxiliar para enviar mensagem para RabbitMQ
    private void sendRabbitMQMessage(Long paymentId, String message) {
        RabbitMQMessage rabbitMQMessage = new RabbitMQMessage();
        rabbitMQMessage.setMessageContent(message + paymentId);
        rabbitMQMessage.setStatus(MessageStatus.SENT); 

        // Publicar a mensagem na fila RabbitMQ
        rabbitTemplate.convertAndSend("payment-", rabbitMQMessage);
        logger.info("Mensagem enviada para RabbitMQ: {}", rabbitMQMessage);
    }

    // Obter pagamento por ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
    }

    // Listar todos os pagamentos
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Atualizar pagamento existente
    @Transactional
    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment payment = getPaymentById(id); 
        payment.setAmount(paymentDetails.getAmount());
        payment.setCurrency(paymentDetails.getCurrency());
        payment.setStatus(paymentDetails.getStatus());
        payment.setPaymentMethod(paymentDetails.getPaymentMethod());
        return paymentRepository.save(payment); 
    }

    // Deletar um pagamento
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id);
        paymentRepository.delete(payment); 
    }

    // Filtrar pagamentos por status
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status); 
    }

    // Processar a verificação de fraude
    @Transactional
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
        
        // Define a data/hora da verificação de fraude
        fraudCheck.setCheckedAt(ZonedDateTime.now());

        // Salva a verificação de fraude no repositório
        fraudCheckRepository.save(fraudCheck);
        
        // Debug: Verifique se a data foi setada corretamente
        logger.info("FraudCheck saved with checkedAt: {}", fraudCheck.getCheckedAt());
    }
}
