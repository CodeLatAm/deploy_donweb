package com.abel.sistema_gestion.dto.vendor;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorRequestBasic {

    @NotNull(message = "El userId es obligatorio")
    private Integer userId;
    @NotNull(message = "El vendorId es obligatorio")
    private Integer vendorId;
}
