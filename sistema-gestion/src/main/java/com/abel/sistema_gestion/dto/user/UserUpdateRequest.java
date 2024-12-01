package com.abel.sistema_gestion.dto.user;

import com.abel.sistema_gestion.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @NotBlank(message = "Nombre es requerido")
    private String name;
    @NotBlank(message = "Email es requerido")
    private String username;
    @NotBlank(message = "Es requerido")
    private String companyName;
    @NotBlank(message = "El plan es requerido")
    private UserStatus userStatus;
}
