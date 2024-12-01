package com.abel.sistema_gestion.dto.mercadoPago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
@Data
@Builder
@NoArgsConstructor

public class WebhookPayload {
    private String action;  // Tipo de acción, como "payment.created", "payment.updated", etc.
    private String externalReference; // ID del usuario o referencia en tu sistema
    private String id;  // ID del pago en Mercado Pago
    private String status;  // Estado del pago (approved, rejected, pending, etc.)
    private String paymentType; // Tipo de pago (credit_card, bank_transfer, etc.)
    private String currency;  // Moneda del pago (ARS, USD, etc.)
    private BigDecimal amount;  // Monto del pago
    private String description;  // Descripción del producto o servicio
    private OffsetDateTime dateCreated;  // Fecha en que se creó el pago
    private OffsetDateTime dateApproved;  // Fecha en que fue aprobado el pago (si es que se aprobó)
    private OffsetDateTime dateLastUpdated;  // Fecha en que se actualizó por última vez el estado del pago
    private String merchantOrderId; // ID de la orden de compra
    private String payerEmail; // Email del pagador
    private String payerName;  // Nombre del pagador
    private String payerId; // ID del pagador en Mercado Pago
    private Boolean subscriptionPayment;  // Indica si es un pago recurrente de suscripción
    private String notificationUrl;  // URL donde se envió el webhook

    public WebhookPayload(String action, String externalReference, String id, String status, String paymentType, String currency, BigDecimal amount, String description, OffsetDateTime dateCreated, OffsetDateTime dateApproved, OffsetDateTime dateLastUpdated, String merchantOrderId, String payerEmail, String payerName, String payerId, Boolean subscriptionPayment, String notificationUrl) {
        this.action = action;
        this.externalReference = externalReference;
        this.id = id;
        this.status = status;
        this.paymentType = paymentType;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateApproved = dateApproved;
        this.dateLastUpdated = dateLastUpdated;
        this.merchantOrderId = merchantOrderId;
        this.payerEmail = payerEmail;
        this.payerName = payerName;
        this.payerId = payerId;
        this.subscriptionPayment = subscriptionPayment;
        this.notificationUrl = notificationUrl;
    }
}
