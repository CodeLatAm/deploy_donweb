package com.abel.sistema_gestion.dto.payment;


import com.abel.sistema_gestion.dto.user.UserBasicInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseFull {

    private Long id;

    private String paymentId;

    private BigDecimal amount;

    private String currency;

    private String status;

    private String dateCreated;

    private String description;

    private UserBasicInfoResponse userBasicInfoResponse;
}
