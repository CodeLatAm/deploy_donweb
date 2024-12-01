package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.mercadoPago.SubscriptionRequest;
import com.abel.sistema_gestion.dto.mercadoPago.WebhookPayload;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import java.util.Map;

public interface MercadoPagoService {
    //void handlePaymentNotification(WebhookPayload payload) throws MPException, MPApiException;

    String createSubscriptionPayment(SubscriptionRequest request) throws MPException, MPApiException;

    boolean processNotificationWebhook(Map<String, Object> values);

    MessageResponse generateMonthlyPaymentLink();

    MessageResponse checkAndExpirePremiumUsers();
}
