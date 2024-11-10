package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.UUID;

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

import com.apifinance.jpa.models.PaymentMethod;
import com.apifinance.jpa.services.PaymentMethodService;

@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        try {
            List<PaymentMethod> paymentMethods = paymentMethodService.findAll();
            return ResponseEntity.ok(paymentMethods);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPaymentMethodById(@PathVariable UUID id) {
        try {
            PaymentMethod paymentMethod = paymentMethodService.findById(id);
            if (paymentMethod != null) {
                return ResponseEntity.ok(paymentMethod);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Método de pagamento não encontrado com ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar o método de pagamento: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        try {
            PaymentMethod createdPaymentMethod = paymentMethodService.save(paymentMethod);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Método de pagamento criado com sucesso. ID: " + createdPaymentMethod.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar o método de pagamento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePaymentMethod(@PathVariable UUID id, @RequestBody PaymentMethod paymentMethod) {
        try {
            paymentMethod.setId(id);
            PaymentMethod savedPaymentMethod = paymentMethodService.save(paymentMethod);
            return ResponseEntity.ok("Método de pagamento atualizado com sucesso. ID: " + savedPaymentMethod.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar o método de pagamento: " + e.getMessage());
        }
    }
}
