package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.Payment;
import com.apifinance.jpa.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(HttpStatus.CREATED).body("Pagamento criado com sucesso. ID do pagamento: " + createdPayment.getId());
        } catch (Exception e) {
            logger.error("Erro ao criar pagamento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar pagamento: " + e.getMessage());
        }
    }

    // Aqui você pode adicionar outros métodos para buscar, atualizar ou excluir pagamentos
}
