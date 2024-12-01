package com.abel.sistema_gestion.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateRequest {

    @NotBlank(message = "Nombre es requerido")
    private String name;
    @NotBlank(message = "Dirección es requerido")
    private String address;
    @NotBlank(message = "celular es requerido")
    private String phone;
    @Email
    @NotBlank(message = "Email es requerido")
    private String email;
    @NotBlank(message = "cp es requerido")
    private String cp;
    @NotBlank(message = "Entre calles requerido")
    private String betweenStreets;
    @NotBlank(message = "Localidad requerida")
    private String location;
    @NotBlank(message = "Provincia es requerida")
    private String province;
    @NotBlank(message = "DNI es requerido")
    private String dni;

}
