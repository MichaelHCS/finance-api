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

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, 
                          RabbitTemplate rabbitTemplate,
                          RabbitMQMessageRepository rabbitmqMessageRepository,
                          TransactionLogRepository transactionLogRepository,
                          PaymentMethodRepository paymentMethodRepository) {

        this.paymentRepository = paymentRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitmqMessageRepository = rabbitmqMessageRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Transactional
    public Payment createPayment(Payment payment) {
        // Define o status inicial do pagamento como PENDING
        payment.setStatus(PaymentStatus.PENDING);

        // Verifica se já existe uma mensagem RabbitMQ associada ao pagamento
        RabbitMQMessage rabbitMQMessage = payment.getRabbitMQMessage();
        if (rabbitMQMessage == null) {
            rabbitMQMessage = new RabbitMQMessage();
            rabbitMQMessage.setMessageContent("Payment Created");
            rabbitMQMessage.setStatus(rabbitmqMessageStatus.PENDING);
            rabbitMQMessage.setSentAt(ZonedDateTime.now());
        }

        RabbitMQMessage savedMessage = rabbitmqMessageRepository.save(rabbitMQMessage);
        payment.setRabbitMQMessage(savedMessage);

        Payment savedPayment = paymentRepository.save(payment);

        // Cria o TransactionLog
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setPayment(savedPayment);
        transactionLog.setAction(TransactionAction.PAYMENT_CREATED);
        transactionLog.setDetails(PaymentMethodDetailsType.BANK.name());
        transactionLog.setTimestamp(ZonedDateTime.now());
        transactionLogRepository.save(transactionLog);

        // Envia a mensagem para RabbitMQ
        sendRabbitMQMessage(savedMessage);

    

        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setType(PaymentMethodType.CREDIT_CARD); // Defina o tipo de pagamento
        paymentMethod.setDetails(PaymentMethodDetailsType.BANK.name());
        paymentMethod.addPayment(savedPayment);

        paymentMethodRepository.save(paymentMethod);

        return savedPayment;
    }

    private void sendRabbitMQMessage(RabbitMQMessage rabbitMQMessage) {
        try {
            String messageText = rabbitMQMessage.getMessageContent();
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

    @Transactional
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + paymentId));

        paymentRepository.delete(payment);
        logger.info("Pagamento excluído com sucesso: " + payment);
    }
}