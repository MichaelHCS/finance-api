package com.apifinance.jpa.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.models.PaymentMethod;
import com.apifinance.jpa.repositories.PaymentMethodRepository;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod findById(UUID id) {
        return paymentMethodRepository.findById(id).orElse(null);
    }

    public PaymentMethod save(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

}
