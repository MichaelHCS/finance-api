package com.apifinance.jpa.services;

import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.PaymentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import com.apifinance.jpa.enums.PaymentStatus;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    
    private final PaymentRepository paymentRepository;
    private final RabbitMqService rabbitMqService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, RabbitMqService rabbitMqService) {
        this.paymentRepository = paymentRepository;
        this.rabbitMqService = rabbitMqService;
    }

    public Payment createPayment(Payment payment) {
        // Definindo status e horários iniciais
        logger.info("Criando pagamento: {}", payment); // Log do pagamento recebido
        payment.setPaymentStatus(PaymentStatus.PENDING); // Define o status inicial
        payment.setCreatedAt(ZonedDateTime.now()); // Define a data de criação
        payment.setUpdatedAt(ZonedDateTime.now()); // Define a data de atualização inicial
        
        // Salva o pagamento no banco de dados
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Pagamento criado com sucesso: {}", savedPayment); // Log do pagamento salvo

        // Publica uma mensagem na fila RabbitMQ
        String message = "Pagamento criado com ID: " + savedPayment.getId() + " e status: " + savedPayment.getPaymentStatus();
        rabbitMqService.publishMessage(message);

        return savedPayment;
    }

    // Aqui você pode adicionar outros métodos para atualizar, excluir, ou buscar pagamentos
}
