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

import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.requests.FraudCheckRequest;
import com.apifinance.jpa.services.FraudCheckService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fraud-check")
public class FraudCheckController {

    @Autowired
    private FraudCheckService fraudCheckService;

    // Criar uma nova verificação de fraude
    @PostMapping
    public ResponseEntity<FraudCheck> createFraudCheck(@Valid @RequestBody FraudCheckRequest request) {
        FraudCheck createdFraudCheck = fraudCheckService.createFraudCheck(
            request.getPaymentId(),
            request.getFraudStatus(),
            request.getCheckReasonAsEnum() // Chame o método que converte para Enum
        );
        return ResponseEntity.status(201).body(createdFraudCheck);
    }

    // Obter todas as verificações de fraude
    @GetMapping
    public ResponseEntity<List<FraudCheck>> getAllFraudChecks() {
        List<FraudCheck> fraudChecks = fraudCheckService.getAllFraudChecks();
        return ResponseEntity.ok(fraudChecks);
    }

    // Obter verificação de fraude por ID
    @GetMapping("/{id}")
    public ResponseEntity<FraudCheck> getFraudCheckById(@PathVariable Long id) {
        Optional<FraudCheck> fraudCheck = fraudCheckService.getFraudCheckById(id);
        return fraudCheck.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar verificação de fraude
    @PutMapping("/{id}")
    public ResponseEntity<FraudCheck> updateFraudCheck(@PathVariable Long id, 
                                                        @Valid @RequestBody FraudCheckRequest request) {
        if (!fraudCheckService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        FraudCheck updatedFraudCheck = fraudCheckService.updateFraudCheck(
            id, 
            request.getPaymentId(), 
            request.getFraudStatus(), 
            request.getCheckReasonAsEnum() // Chame o método que converte para Enum
        );
        return ResponseEntity.ok(updatedFraudCheck);
    }

    // Deletar verificação de fraude
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFraudCheck(@PathVariable Long id) {
        if (!fraudCheckService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fraudCheckService.deleteFraudCheck(id);
        return ResponseEntity.noContent().build();
    }
}
