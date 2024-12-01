package com.abel.sistema_gestion.dto.auth;

import com.abel.sistema_gestion.enums.Role;
import com.abel.sistema_gestion.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private Integer user_id;
    private String token;
    private Role role;
    private Integer vendorId;
    private Integer cartId;
    private UserStatus userStatus;
}
