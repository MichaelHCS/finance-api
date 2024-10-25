package com.apifinance.jpa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.services.FraudCheckService;

@RestController
@RequestMapping("/fraud-checks")
public class FraudCheckController {

    @Autowired
    private FraudCheckService fraudCheckService;

    // Endpoint para buscar todos os registros de fraude
    @GetMapping
    public ResponseEntity<List<FraudCheck>> findAll() {
        List<FraudCheck> fraudChecks = fraudCheckService.findAll();
        return new ResponseEntity<>(fraudChecks, HttpStatus.OK);
    }

    // Endpoint para buscar um registro de fraude por ID
    @GetMapping("/{id}")
    public ResponseEntity<FraudCheck> findById(@PathVariable Long id) {
        FraudCheck fraudCheck = fraudCheckService.findById(id);
        return fraudCheck != null ? new ResponseEntity<>(fraudCheck, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para criar um novo registro de fraude
    @PostMapping
    public ResponseEntity<FraudCheck> create(@RequestBody FraudCheck fraudCheck) {
        FraudCheck savedFraudCheck = fraudCheckService.save(fraudCheck);
        return new ResponseEntity<>(savedFraudCheck, HttpStatus.CREATED);
    }

    // Endpoint para atualizar um registro de fraude
    @PutMapping("/{id}")
    public ResponseEntity<FraudCheck> update(@PathVariable Long id, @RequestBody FraudCheck fraudCheck) {
        fraudCheck.setId(id); // Definindo o ID no objeto recebido
        FraudCheck updatedFraudCheck = fraudCheckService.save(fraudCheck);
        return new ResponseEntity<>(updatedFraudCheck, HttpStatus.OK);
    }

    // Endpoint para excluir um registro de fraude por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        fraudCheckService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
