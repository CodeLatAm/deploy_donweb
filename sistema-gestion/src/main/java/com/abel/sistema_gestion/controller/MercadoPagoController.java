package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.mercadoPago.SubscriptionRequest;
import com.abel.sistema_gestion.dto.mercadoPago.WebhookPayload;
import com.abel.sistema_gestion.serviceimpl.service.MercadoPagoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/mercado-pago")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;
/*
    @PostMapping("/notify")
    public ResponseEntity<?> handlePaymentNotification(@RequestBody WebhookPayload payload) throws MPException, MPApiException {
        log.info("En controller handlePaymentNotification()");
        mercadoPagoService.handlePaymentNotification(payload);

        return ResponseEntity.ok("Webhook received");
    }

 */
    @PreAuthorize("hasAnyAuthority('PROPIETARIO')")
    @PostMapping("/subscription/create")
    public ResponseEntity<String> createSubscription(@RequestBody SubscriptionRequest request) throws MPException, MPApiException {
        return ResponseEntity.ok(mercadoPagoService.createSubscriptionPayment(request));
    }

    @PostMapping("/notify/payment")
    public ResponseEntity<String> mercadoPagoNotificationPayment(@RequestBody Map<String, Object> values) {
        log.info("Entrando al controller mercadoPagoNotificationPayment()");
        if(mercadoPagoService.processNotificationWebhook(values)) {

            return ResponseEntity.ok("ACCEPTED");
        }
        else
            return new ResponseEntity<>(  "PAYMENT REQUIERED", HttpStatus.PAYMENT_REQUIRED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/generate-monthly-links")
    public ResponseEntity<MessageResponse> generateMonthlyPaymentLinks() {
        return ResponseEntity.ok(mercadoPagoService.generateMonthlyPaymentLink());

    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/premium/check-expiration")
    public ResponseEntity<MessageResponse> checkAndExpirePremiumUsers() {
        return ResponseEntity.ok(mercadoPagoService.checkAndExpirePremiumUsers());
    }
}
