package com.abel.sistema_gestion.dto.auth;

import com.abel.sistema_gestion.enums.Role;
import com.abel.sistema_gestion.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    private String token;
    private String message;
    private HttpStatus httpStatus;
    private Role role;
    private UserStatus userStatus;
}
