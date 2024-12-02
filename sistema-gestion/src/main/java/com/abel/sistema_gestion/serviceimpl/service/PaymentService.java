package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.payment.PaymentResponse;
import com.abel.sistema_gestion.dto.payment.PaymentResponseFull;
import com.abel.sistema_gestion.entity.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    void save(Payment payment);

    List<PaymentResponse> getAllPaymentsByUserId(Integer userId);


    List<PaymentResponseFull> getAllPayments();

    Map<Integer, BigDecimal> getEarningsByYear();
}
