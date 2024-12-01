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
public class VendorRequest {

    @NotNull(message = "El userId es obligatorio")
    private Integer userId;
    @NotBlank(message = "Nombre es requerido")
    private String name;
    @NotBlank(message = "Apellido es obligatorio")
    private String surname;
    @NotBlank(message = "El password es requerido")
    @Size(min = 8)
    private String password;
    @NotBlank(message = "Email es obligatorio")
    @Email()
    private String email;
    @NotBlank(message = "Direccion es requerida")
    private String address;
    @NotBlank(message = "Plan es requerido")
    private String plan;

}
