package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckResult;
import com.apifinance.jpa.enums.PaymentMethodDetailsType;
import com.apifinance.jpa.enums.PaymentMethodType;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.TransactionAction;
import com.apifinance.jpa.enums.rabbitmqMessageStatus;
import com.apifinance.jpa.exceptions.PaymentNotFoundException;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.PaymentMethod;
import com.apifinance.jpa.models.RabbitMQMessage;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.rabbitmqConfig.RabbitConfig;
import com.apifinance.jpa.repositories.PaymentMethodRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMQMessageRepository;
import com.apifinance.jpa.repositories.TransactionLogRepository;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQMessageRepository rabbitmqMessageRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final FraudCheckService fraudCheckService;

    private ZonedDateTime processedAt;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, RabbitTemplate rabbitTemplate,
                          RabbitMQMessageRepository rabbitmqMessageRepository,
                          TransactionLogRepository transactionLogRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          FraudCheckService fraudCheckService) {

        this.paymentRepository = paymentRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitmqMessageRepository = rabbitmqMessageRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.fraudCheckService = fraudCheckService;
    }

    @Transactional
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + paymentId));

        paymentRepository.delete(payment);
        logger.info("Pagamento excluído com sucesso: " + payment);
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        payment.setStatus(PaymentStatus.PENDING);

        RabbitMQMessage rabbitMQMessage = new RabbitMQMessage();
        rabbitMQMessage.setMessageContent("Payment Created");
        rabbitMQMessage.setStatus(rabbitmqMessageStatus.PENDING);
        rabbitMQMessage.setSentAt(ZonedDateTime.now());
        rabbitMQMessage.setProcessedAt(processedAt);
        RabbitMQMessage savedMessage = rabbitmqMessageRepository.save(rabbitMQMessage);
        payment.setRabbitMQMessage(savedMessage);
        
        Payment savedPayment = paymentRepository.save(payment);

        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setPayment(savedPayment);
        transactionLog.setAction(TransactionAction.PAYMENT_CREATED);
        transactionLog.setTimestamp(ZonedDateTime.now());
        transactionLog.setDetails(PaymentMethodDetailsType.BANK.name());
        transactionLogRepository.save(transactionLog);

        sendRabbitMQMessage(savedMessage);

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType(PaymentMethodType.CREDIT_CARD);
        paymentMethod.setDetails(PaymentMethodDetailsType.BANK.name());

        paymentMethodRepository.save(paymentMethod);

        return savedPayment;
    }

    public void processFraudCheck(Long paymentId, FraudCheckResult fraudStatus, FraudCheckReason checkReason) {
        fraudCheckService.processFraudCheck(paymentId, fraudStatus, checkReason);
    }

    private void sendRabbitMQMessage(RabbitMQMessage rabbitMQMessage) {
        try {
            String messageText = "Payment Created";
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.PAYMENT_ROUTING_KEY, messageText);
            logger.info("Message sent to RabbitMQ: {}", messageText);

            rabbitMQMessage.setStatus(rabbitmqMessageStatus.SENT);
            rabbitMQMessage.setProcessedAt(ZonedDateTime.now());
            rabbitmqMessageRepository.save(rabbitMQMessage);
        } catch (AmqpException e) {
            logger.error("Error sending message to RabbitMQ: {}", e.getMessage());

            rabbitMQMessage.setStatus(rabbitmqMessageStatus.ERROR);
            rabbitMQMessage.setProcessedAt(ZonedDateTime.now());
            rabbitmqMessageRepository.save(rabbitMQMessage);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
        }
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional
    public Payment updatePayment(Long id, Payment payment) {
        Payment existingPayment = getPaymentById(id);
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setStatus(payment.getStatus());
        return paymentRepository.save(existingPayment);
    }
}
