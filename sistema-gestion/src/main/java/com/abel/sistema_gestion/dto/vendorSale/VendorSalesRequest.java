package com.abel.sistema_gestion.dto.vendorSale;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorSalesRequest {
    @NotBlank(message = "Zona de envío es requerida")
    private String shippingArea;
    @NotBlank(message = "Tipo de pago es requerida")
    private String paymentType;
}
