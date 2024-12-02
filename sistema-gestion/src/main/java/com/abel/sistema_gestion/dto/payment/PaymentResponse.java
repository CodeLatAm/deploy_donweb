package com.abel.sistema_gestion.dto.payment;


import lombok.*;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;

    private String paymentId;

    private BigDecimal amount;

    private String currency;

    private String status;

    private String dateCreated;

    private String description;
}
