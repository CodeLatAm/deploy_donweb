package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.paymentLinks.PaymentLinkResponse;

import java.util.List;

public interface PaymentLinkService {
    List<PaymentLinkResponse> getAllPaymentLinksByUserId(Integer userId);
}
