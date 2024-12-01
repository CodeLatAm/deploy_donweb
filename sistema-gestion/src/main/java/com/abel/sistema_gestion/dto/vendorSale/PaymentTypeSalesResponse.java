package com.abel.sistema_gestion.dto.vendorSale;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeSalesResponse {
    private String paymentType;
    private Long totalSales;
}
