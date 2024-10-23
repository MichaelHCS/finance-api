package com.apifinance.jpa.services;

import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FraudCheckService {

    @Autowired
    private FraudCheckRepository fraudCheckRepository;

    public List<FraudCheck> findAll() {
        return fraudCheckRepository.findAll();
    }

    public FraudCheck findById(Long id) {
        return fraudCheckRepository.findById(id).orElse(null);
    }

    public FraudCheck save(FraudCheck fraudCheck) {
        return fraudCheckRepository.save(fraudCheck);
    }

    public void deleteById(Long id) {
        fraudCheckRepository.deleteById(id);
    }
}
