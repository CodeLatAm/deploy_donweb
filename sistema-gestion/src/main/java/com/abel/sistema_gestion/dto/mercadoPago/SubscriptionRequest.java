package com.abel.sistema_gestion.dto.mercadoPago;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {

    @NotNull(message = "userId es requerido")
    private Integer userId;
    @NotNull(message = "price es requerido")
    private BigDecimal price;
    @NotBlank(message = "title es requerido")
    private String title;//"Suscripción Premium"
    @NotBlank(message = "description es requerido")
    private String description;//"Acceso a funcionalidades premium"


}
