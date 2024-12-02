package com.abel.sistema_gestion.dto.cartItem;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

    @NotNull(message = "La cantidad no puede ser null")
    @Min(value = 1, message = "La cantidad debe ser > 0")
    private Integer quantity;
    @NotNull(message = "El total no puede ser null")
    private Double total;
}
