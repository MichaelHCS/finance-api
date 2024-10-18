package com.apifinance.jpa.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.requests.FraudCheckRequest;
import com.apifinance.jpa.services.FraudCheckService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fraud-check")
public class FraudCheckController {

    @Autowired
    private FraudCheckService fraudCheckService;

    @Autowired
    private FraudCheckRepository fraudCheckRepository;

    // Criar uma nova verificação de fraude
    @PostMapping
    public ResponseEntity<FraudCheck> createFraudCheck(@Valid @RequestBody FraudCheckRequest request) {
        FraudCheck createdFraudCheck = fraudCheckService.createFraudCheck(request);
        return ResponseEntity.status(201).body(createdFraudCheck);
    }

    // Obter todas as verificações de fraude
    @GetMapping
    public ResponseEntity<List<FraudCheck>> getAllFraudChecks() {
        List<FraudCheck> fraudChecks = fraudCheckRepository.findAll();
        return ResponseEntity.ok(fraudChecks);
    }

    // Obter verificação de fraude por ID
    @GetMapping("/{id}")
    public ResponseEntity<FraudCheck> getFraudCheckById(@PathVariable Long id) {
        return fraudCheckRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Excluir verificação de fraude
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFraudCheck(@PathVariable Long id) {
        if (!fraudCheckRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fraudCheckRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
