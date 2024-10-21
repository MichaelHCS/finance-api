package com.apifinance.jpa.controllers;

import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.services.FraudCheckService; // Supondo que você tenha um FraudCheckService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fraud-checks")
public class FraudCheckController {

    private final FraudCheckService fraudCheckService;

    @Autowired
    public FraudCheckController(FraudCheckService fraudCheckService) {
        this.fraudCheckService = fraudCheckService;
    }

    @GetMapping
    public ResponseEntity<List<FraudCheck>> getAllFraudChecks() {
        List<FraudCheck> fraudChecks = fraudCheckService.findAll();
        return ResponseEntity.ok(fraudChecks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraudCheck> getFraudCheckById(@PathVariable Long id) {
        FraudCheck fraudCheck = fraudCheckService.findById(id);
        return fraudCheck != null ? ResponseEntity.ok(fraudCheck) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<FraudCheck> createFraudCheck(@RequestBody FraudCheck fraudCheck) {
        FraudCheck createdFraudCheck = fraudCheckService.save(fraudCheck);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFraudCheck);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FraudCheck> updateFraudCheck(@PathVariable Long id, @RequestBody FraudCheck fraudCheck) {
        FraudCheck updatedFraudCheck = fraudCheckService.update(id, fraudCheck);
        return updatedFraudCheck != null ? ResponseEntity.ok(updatedFraudCheck) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFraudCheck(@PathVariable Long id) {
        return fraudCheckService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
