package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.enums.rabbitmqMessageStatus;
import com.apifinance.jpa.exceptions.PaymentNotFoundException;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.models.RabbitMQMessage;
import com.apifinance.jpa.rabbitmqConfig.RabbitConfig;
import com.apifinance.jpa.repositories.PaymentRepository;
import com.apifinance.jpa.repositories.RabbitMQMessageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQMessageRepository rabbitmqMessageRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, RabbitTemplate rabbitTemplate,
                          RabbitMQMessageRepository rabbitmqMessageRepository) {
        this.paymentRepository = paymentRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitmqMessageRepository = rabbitmqMessageRepository;
    }

    // Criar pagamento com status "Aguardando análise de fraude" e enviar mensagem RabbitMQ
    @Transactional
    public Payment createPayment(Payment payment) {
        // Definir status inicial do pagamento
        payment.setStatus(PaymentStatus.PENDING); // Ajustar para o status desejado
        Payment savedPayment = paymentRepository.save(payment);

        // Criar registro na tabela rabbitmq_message
        RabbitMQMessage rabbitMQMessage = new RabbitMQMessage();
        rabbitMQMessage.setMessageContent("Payment Created");
        rabbitMQMessage.setStatus(rabbitmqMessageStatus.PENDING);
        rabbitMQMessage.setSentAt(ZonedDateTime.now());
        rabbitmqMessageRepository.save(rabbitMQMessage);

        // Enviar mensagem para RabbitMQ
        sendRabbitMQMessage(rabbitMQMessage);

        return savedPayment; // Retorna o pagamento salvo
    }

    // Método para enviar mensagens para o RabbitMQ com tratamento de erros e tentativa de reenvio
    private void sendRabbitMQMessage(RabbitMQMessage rabbitMQMessage) {
        try {
            // Enviar a mensagem para RabbitMQ
            String messageText = "Payment Created"; // Texto simples
            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.PAYMENT_ROUTING_KEY, messageText);
            logger.info("Message sent to RabbitMQ: {}", messageText);

            // Atualizar o status para enviado após sucesso
            rabbitMQMessage.setStatus(rabbitmqMessageStatus.SENT);
            rabbitmqMessageRepository.save(rabbitMQMessage);
        } catch (AmqpException e) {
            logger.error("Error sending message to RabbitMQ: {}", e.getMessage());

            // Atualizar o status para erro em caso de falha
            rabbitMQMessage.setStatus(rabbitmqMessageStatus.ERROR);
            rabbitmqMessageRepository.save(rabbitMQMessage);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
        }
    }

    // Buscar pagamento por ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id " + id));
    }

    // Listar todos os pagamentos
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Atualizar pagamento
    @Transactional
    public Payment updatePayment(Long id, Payment payment) {
        Payment existingPayment = getPaymentById(id);
        existingPayment.setAmount(payment.getAmount());
        existingPayment.setStatus(payment.getStatus());
        return paymentRepository.save(existingPayment);
    }

    // Deletar pagamento
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id);
        paymentRepository.delete(payment);
    }
}
