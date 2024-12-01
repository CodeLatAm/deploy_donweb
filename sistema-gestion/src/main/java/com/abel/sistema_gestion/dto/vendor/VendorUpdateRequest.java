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
public class VendorUpdateRequest {

    @NotNull(message = "El userId es requerido")
    private Integer userId;
    @NotNull(message = "El vendorId es requerido")
    private Integer id;
    @NotBlank(message = "Nombre es requerido")
    private String name;
    @NotBlank(message = "Apellido es obligatorio")
    private String surname;
    @NotBlank(message = "Email es obligatorio")
    @Email()
    private String email;
}
