package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.exceptions.ResourceNotFoundException;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.repositories.PaymentRepository;

import jakarta.transaction.Transactional;

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

        logger.info("Horário definido para createdAt: {}", payment.getCreatedAt());
        logger.info("Horário definido para updatedAt: {}", payment.getUpdatedAt());

        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Pagamento criado com sucesso: {}", savedPayment);

        String message = savedPayment.getId().toString();
        rabbitMqService.publishMessage(message);

        return savedPayment;
    }

    public Payment getPaymentById(UUID paymentId) {
        logger.info("Recuperando pagamento com ID: {}", paymentId);
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    logger.error("Pagamento não encontrado com o ID: {}", paymentId);
                    return new ResourceNotFoundException("Pagamento não encontrado com o ID: " + paymentId);
                });
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        logger.info("Total de pagamentos recuperados: {}", payments.size());
        return payments;
    }

    @Transactional
    public Payment updatePayment(UUID paymentId, Payment paymentDetails) {

        logger.info("Atualizando pagamento com ID: {}", paymentId);
        logger.info("Payload recebido para atualização de pagamento: {}", paymentDetails);

        // Recuperando o pagamento existente
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    logger.error("Pagamento não encontrado com ID: {}", paymentId);
                    return new ResourceNotFoundException("Pagamento não encontrado com ID: " + paymentId);
                });

        logger.info("Pagamento existente antes da atualização: {}", existingPayment);

        existingPayment.setAmount(paymentDetails.getAmount());
        existingPayment.setCurrency(paymentDetails.getCurrency());
        existingPayment.setPaymentType(paymentDetails.getPaymentType());

        ZonedDateTime newUpdatedAt = ZonedDateTime.now();
        existingPayment.setUpdatedAt(newUpdatedAt);

        logger.info("Horário definido para updatedAt antes da persistência: {}", newUpdatedAt);

        Payment updatedPayment = paymentRepository.saveAndFlush(existingPayment);

        logger.info("Horário persistido no banco para updatedAt após a atualização: {}", updatedPayment.getUpdatedAt());

        String messageContent = updatedPayment.getId().toString();
        rabbitMqService.publishMessage(messageContent);
        logger.info("Mensagem enviada para o RabbitMQ com ID do pagamento: {}", updatedPayment.getId());

        return updatedPayment;
    }


}
