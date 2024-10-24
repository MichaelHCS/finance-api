package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.PaymentMethod;
import com.apifinance.jpa.services.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethod paymentMethod = paymentMethodService.findById(id);
        return paymentMethod != null ? ResponseEntity.ok(paymentMethod) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public PaymentMethod createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        return paymentMethodService.save(paymentMethod);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethod paymentMethod) {
        paymentMethod.setId(id);
        PaymentMethod updatedPaymentMethod = paymentMethodService.save(paymentMethod);
        return ResponseEntity.ok(updatedPaymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
