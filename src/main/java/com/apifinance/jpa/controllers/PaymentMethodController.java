package com.apifinance.jpa.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apifinance.jpa.models.PaymentMethod;
import com.apifinance.jpa.repositories.PaymentMethodRepository;

@RestController
@RequestMapping("/payment-method")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    // Criar um novo método de pagamento
    @PostMapping
    public ResponseEntity<PaymentMethod> createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            return ResponseEntity.badRequest().build();
        }
        PaymentMethod createdPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.status(201).body(createdPaymentMethod);
    }

    // Obter todos os métodos de pagamento
    @GetMapping
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        return ResponseEntity.ok(paymentMethods);
    }

    // Obter método de pagamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isPresent()) {
            return ResponseEntity.ok(paymentMethod.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Atualizar método de pagamento
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethod paymentMethod) {
        if (!paymentMethodRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paymentMethod.setId(id);
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.ok(updatedPaymentMethod);
    }

    // Deletar método de pagamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        if (!paymentMethodRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paymentMethodRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
