package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.models.TransactionLog;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMqMessageRepository;
import com.apifinance.jpa.repositories.TransactionLogRepository;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.RabbitMqMessageStatus;
import com.apifinance.jpa.enums.TransactionAction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apifinance.jpa.rabbitmqconfig.RabbitConfig;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RabbitMqMessageRepository rabbitMqMessageRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          RabbitMqMessageRepository rabbitMqMessageRepository,
                          TransactionLogRepository transactionLogRepository,
                          RabbitTemplate rabbitTemplate) {
        this.paymentRepository = paymentRepository;
        this.rabbitMqMessageRepository = rabbitMqMessageRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Payment findById(Long id) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        return optionalPayment.orElse(null);
    }

    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    public Payment update(Long id, Payment payment) {
        if (paymentRepository.existsById(id)) {
            payment.setUpdatedAt(ZonedDateTime.now());
            return paymentRepository.save(payment);
        }
        return null;
    }

    public boolean delete(Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Payment createPayment(Payment payment) {
        // Defina o status do pagamento
        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus(PaymentStatus.PENDING); // Define um valor padrão se não estiver definido
        }
        
        // Salva o pagamento no repositório
        Payment savedPayment = paymentRepository.save(payment);
        // Criar log de transação
        createTransactionLog(savedPayment, TransactionAction.PAYMENT_CREATED, "Pagamento criado com sucesso.");

        // Publica mensagem na fila RabbitMQ
        String messageContent = createMessageContent(savedPayment);
        publishToRabbitMq(messageContent);

        // Cria um registro correspondente na tabela rabbitmq_message
        RabbitMqMessage rabbitMqMessage = new RabbitMqMessage();
        rabbitMqMessage.setMessageContent(messageContent);
        rabbitMqMessage.setStatus(RabbitMqMessageStatus.SENT);
        rabbitMqMessage.setSentAt(ZonedDateTime.now());
        rabbitMqMessageRepository.save(rabbitMqMessage);

        return savedPayment;
    }

    private void createTransactionLog(Payment payment, TransactionAction action, String details) {
        TransactionLog transactionLog = new TransactionLog(payment, action, details);
        transactionLogRepository.save(transactionLog);
    }

    private String createMessageContent(Payment payment) {
        return "Pagamento criado: " + payment.toString();
    }

    private void publishToRabbitMq(String messageContent) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.PAYMENT_ROUTING_KEY, messageContent);
    }
}
