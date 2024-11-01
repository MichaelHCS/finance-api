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
     
        logger.info("Criando pagamento: {}", payment); 
        payment.setPaymentStatus(PaymentStatus.PENDING); 
        payment.setCreatedAt(ZonedDateTime.now()); 
        payment.setUpdatedAt(ZonedDateTime.now()); 
        
        
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Pagamento criado com sucesso: {}", savedPayment); 

        
        String message = "Pagamento criado com ID: " + savedPayment.getId() + " e status: " + savedPayment.getPaymentStatus();
        rabbitMqService.publishMessage(message);

        return savedPayment;
    }

    // Aqui você pode adicionar outros métodos para atualizar, excluir, ou buscar pagamentos
}
