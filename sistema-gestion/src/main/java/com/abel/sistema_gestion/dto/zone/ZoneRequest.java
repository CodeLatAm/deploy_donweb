package com.abel.sistema_gestion.dto.zone;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneRequest {

    @NotBlank(message = "Codigo postal es requerido")
    private String cp;
    @NotBlank(message = "Localidad es requerida")
    private String location;
    @NotNull @Min(0)
    private Double cost;
    @NotNull(message = "El userId es requerido")
    private Integer userId;
    @NotBlank(message = "Provincia es obligatoria")
    private String province;
}
