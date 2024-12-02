package com.abel.sistema_gestion.dto.cart;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartQuantityRequest {
    @NotNull
    private Integer cartId;
    @NotNull
    private Integer vendorId;
}
