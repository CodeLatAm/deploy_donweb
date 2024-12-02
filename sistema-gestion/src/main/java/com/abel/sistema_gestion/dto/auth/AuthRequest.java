package com.abel.sistema_gestion.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    //@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "El email debe tener un formato válido")
    private String username;
    @NotBlank(message = "El password es requerido")
    @Size(min = 8)
    private String password;


}
