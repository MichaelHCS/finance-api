package com.apifinance.jpa.controllers;

//import com.apifinance.jpa.enums.FraudCheckReason;
//import com.apifinance.jpa.enums.FraudCheckStatus;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.services.FraudCheckService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import com.apifinance.jpa.models.RabbitMqMessage;

@RestController
@RequestMapping("/fraud-checks")
public class FraudCheckController {

    @Autowired
    private FraudCheckService fraudCheckService;

    @GetMapping
    public List<FraudCheck> getAllFraudChecks() {
        return fraudCheckService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraudCheck> getFraudCheckById(@PathVariable Long id) {
        FraudCheck fraudCheck = fraudCheckService.findById(id);
        return fraudCheck != null ? ResponseEntity.ok(fraudCheck) : ResponseEntity.notFound().build();
    }

    @PostMapping
    

    @PutMapping("/{id}")
    public ResponseEntity<FraudCheck> updateFraudCheck(@PathVariable Long id, @RequestBody FraudCheck fraudCheck) {
        fraudCheck.setId(id);
        FraudCheck updatedFraudCheck = fraudCheckService.save(fraudCheck);
        return ResponseEntity.ok(updatedFraudCheck);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFraudCheck(@PathVariable Long id) {
        fraudCheckService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
