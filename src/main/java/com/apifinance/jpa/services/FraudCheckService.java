package com.apifinance.jpa.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apifinance.jpa.enums.FraudCheckReason;
import com.apifinance.jpa.enums.FraudCheckStatus;
import com.apifinance.jpa.enums.PaymentStatus;
import com.apifinance.jpa.models.FraudCheck;
import com.apifinance.jpa.models.Payment;
//import com.apifinance.jpa.models.RabbitMqMessage;
import com.apifinance.jpa.repositories.FraudCheckRepository;
import com.apifinance.jpa.repositories.PaymentRepository;

@Service
public class FraudCheckService {

    @Autowired
    private FraudCheckRepository fraudCheckRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<FraudCheck> findAll() {
        return fraudCheckRepository.findAll();
    }

    public FraudCheck findById(Long id) {
        return fraudCheckRepository.findById(id).orElse(null);
    }

   
    public void analyzeFraud(Payment payment, boolean isFraudulent, FraudCheckReason checkReason) {
        
        if (isFraudulent) {
            payment.setPaymentStatus(PaymentStatus.REJECTED); 
        } else {
            payment.setPaymentStatus(PaymentStatus.APPROVED); 
        }
    
        paymentRepository.save(payment);
    
        FraudCheck fraudCheck = new FraudCheck();
        fraudCheck.setPayment(payment);
        fraudCheck.setCheckedAt(ZonedDateTime.now()); 
        fraudCheck.setFraudStatus(isFraudulent ? FraudCheckStatus.APPROVED: FraudCheckStatus.REJECTED);
        fraudCheck.setFraudReason(checkReason);
    
        fraudCheckRepository.save(fraudCheck);
    }

    public FraudCheck save(FraudCheck fraudCheck) {
        return fraudCheckRepository.save(fraudCheck);
    }

    public void deleteById(Long id) {
        fraudCheckRepository.deleteById(id);
    }

}


