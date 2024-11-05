package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apifinance.jpa.exceptions.ResourceNotFoundException;
import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.services.PaymentService;
import com.apifinance.jpa.responses.ApiResponse;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createPayment(@RequestBody Payment payment) {
        logger.info("Recebendo requisição para criar pagamento: {}", payment);
        try {
            Payment createdPayment = paymentService.createPayment(payment);
            logger.info("Pagamento criado com sucesso: {}", createdPayment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Pagamento criado com sucesso.", createdPayment.getId()));
        } catch (Exception e) {
            logger.error("Erro ao criar pagamento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Erro ao criar pagamento: " + e.getMessage()));
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable UUID paymentId) {
        logger.info("Buscando pagamento com ID: {}", paymentId);
        try {
            Payment payment = paymentService.getPaymentById(paymentId);
            logger.info("Pagamento encontrado: {}", payment);
            return ResponseEntity.ok(payment);
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
    public ResponseEntity<ApiResponse> updatePayment(@PathVariable UUID paymentId, @RequestBody Payment paymentDetails) {
        logger.info("Recebendo requisição para atualizar pagamento com ID: {}", paymentId);
        try {
            Payment updatedPayment = paymentService.updatePayment(paymentId, paymentDetails);
            logger.info("Pagamento atualizado com sucesso: {}", updatedPayment);
            return ResponseEntity.ok(new ApiResponse("Pagamento atualizado com sucesso.", updatedPayment.getId()));
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Pagamento não encontrado com ID: " + paymentId));
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar pagamento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Erro ao atualizar pagamento: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> deletePayment(@PathVariable UUID paymentId) {
        logger.info("Recebendo requisição para deletar pagamento com ID: {}", paymentId);
        try {
            paymentService.deletePayment(paymentId);
            logger.info("Pagamento com ID {} deletado com sucesso", paymentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse("Pagamento deletado com sucesso."));
        } catch (ResourceNotFoundException e) {
            logger.error("Erro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Pagamento não encontrado com ID: " + paymentId));
        } catch (Exception e) {
            logger.error("Erro inesperado ao deletar pagamento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Erro ao deletar pagamento: " + e.getMessage()));
        }
    }
}
