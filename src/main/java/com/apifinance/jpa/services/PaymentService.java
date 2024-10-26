package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apifinance.jpa.configrabbitmq.RabbitMqConfig;
import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.TransactionAction;
import com.apifinance.jpa.enums.TransactionLogDetails;
import com.apifinance.jpa.models.Customer;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.repositories.CustomerRepository;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.TransactionLogRepository;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final RabbitMqService rabbitMqService;
    private final CustomerRepository customerRepository;
    private final TransactionLogRepository transactionLogRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          RabbitMqService rabbitMqService,
                          CustomerRepository customerRepository,
                          TransactionLogRepository transactionLogRepository) {
        this.paymentRepository = paymentRepository;
        this.rabbitMqService = rabbitMqService;
        this.customerRepository = customerRepository;
        this.transactionLogRepository = transactionLogRepository;
    }

    @Transactional
    public Payment createPayment(Payment payment, Long customerId) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));

        payment.setCustomer(customer);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);

        try {
            publishPaymentMessage(savedPayment);
            createTransactionLog(savedPayment, TransactionAction.PAYMENT_CREATED, null);
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage(), e);
            throw e; // Re-throw to trigger rollback
        }

        return savedPayment;
    }

    public void createTransactionLog(Payment payment, TransactionAction action, FraudCheckReason reason) {
        String message;
    
        if (action == TransactionAction.PAYMENT_CREATED) {
            message = TransactionLogDetails.PAYMENT_CREATED.getDetails(payment, reason);
        } else if (action == TransactionAction.FRAUD_DETECTED) {
            message = TransactionLogDetails.FRAUD_DETECTED.getDetails(payment, reason);
        } else {
            message = "Ação desconhecida"; // Ou outro valor padrão, caso necessário
        }
    
        TransactionLog transactionLog = new TransactionLog(payment, action, ZonedDateTime.now(), message);
        transactionLogRepository.save(transactionLog);
        logger.info("Transaction log created for payment ID: {}", payment.getId());
    }
    


    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    private void publishPaymentMessage(Payment payment) {
        String messageContent = createPaymentMessage(payment);
        String exchange = RabbitMqConfig.EXCHANGE;
        String routingKey = RabbitMqConfig.ROUTING_KEY;

        rabbitMqService.publishMessage(exchange, routingKey, messageContent);
    }

    private String createPaymentMessage(Payment payment) {
        return String.format("{\"id\": \"%s\", \"customerId\": \"%s\", \"status\": \"%s\"}",
                payment.getId(), payment.getCustomer().getId(), payment.getPaymentStatus());
    }
}
