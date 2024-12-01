package com.abel.sistema_gestion.dto.freePrice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeePriceResponse {
    private Long id;
    private BigDecimal price;
}
