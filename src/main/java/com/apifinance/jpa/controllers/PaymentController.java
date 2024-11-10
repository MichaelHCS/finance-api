package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.exceptions.ResourceNotFoundException;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.services.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody Payment payment) {
        logger.info("Recebendo requisição para criar pagamento: {}", payment);
        try {
            Payment createdPayment = paymentService.createPayment(payment);
            logger.info("Pagamento criado com sucesso: {}", createdPayment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Pagamento criado com sucesso. ID: " + createdPayment.getId());
        } catch (Exception e) {
            logger.error("Erro ao criar pagamento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao criar pagamento: " + e.getMessage());
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<String> getPaymentById(@PathVariable UUID paymentId) {
        logger.info("Buscando pagamento com ID: {}", paymentId);
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            logger.info("Pagamento encontrado: {}", payment);
            return ResponseEntity.ok("Pagamento encontrado com sucesso. ID: " + paymentId);
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pagamento não encontrado com ID: " + paymentId);
        }
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        logger.info("Buscando todos os pagamentos");
        List<Payment> payments = paymentService.getAllPayments();
        logger.info("Total de pagamentos encontrados: {}", payments.size());
        return ResponseEntity.ok(payments);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<String> updatePayment(@PathVariable UUID paymentId, @RequestBody Payment paymentDetails) {
        logger.info("Recebendo requisição para atualizar pagamento com ID: {}", paymentId);
        try {
            Payment updatedPayment = paymentService.updatePayment(paymentId, paymentDetails);
            logger.info("Pagamento atualizado com sucesso: {}", updatedPayment);
            return ResponseEntity.ok("Pagamento atualizado com sucesso. ID: " + updatedPayment.getId());
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pagamento não encontrado com ID: " + paymentId);
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar pagamento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar pagamento: " + e.getMessage());
        }
    }
    
}
