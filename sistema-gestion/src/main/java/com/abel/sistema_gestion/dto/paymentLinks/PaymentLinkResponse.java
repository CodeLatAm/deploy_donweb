package com.abel.sistema_gestion.dto.paymentLinks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkResponse {
    private Long id;
    private String paymentUrl;
    private Long userId;
    private String createdDate;
    private boolean paid; // Estado del pago
    private String paymentId;
    private String linkStatus;
}
