package com.abel.sistema_gestion.dto.cartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemTotalResponse {

    private Integer totalProductQuantity;
    private Double totalProductValue;
}