package com.abel.sistema_gestion.mapper;

import com.abel.sistema_gestion.dto.auth.AuthResponse;
import com.abel.sistema_gestion.dto.auth.RegisterRequest;
import com.abel.sistema_gestion.dto.auth.RegisterResponse;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.enums.Role;
import com.abel.sistema_gestion.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Log4j2
@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final PasswordEncoder passwordEncoder;

    public User mapToRegisterRequest(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PROPIETARIO)
                .startOfActivity(LocalDate.now())
                .companyName(request.getCompanyName())
                .userStatus(UserStatus.FREE)
                .verificationToken(false)
                .build();
    }

    public RegisterResponse mapToRegisterResponse(String token) {
        return RegisterResponse.builder()
                .token(token)
                .message("Hemos enviado un correo de validación a su dirección de email. Haga clic en el enlace incluido en el correo para verificar su cuenta.")
                .httpStatus(HttpStatus.CREATED)
                .role(Role.PROPIETARIO)
                .userStatus(UserStatus.FREE)
                .build();
    }

    public AuthResponse mapToAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .user_id(user.getId())
                .role(user.getRole())
                .userStatus(user.getUserStatus())
                .build();
    }

    public AuthResponse mapToAuthResponseVendor(String token, Vendor vendor) {
        log.info(vendor.toString());
        return AuthResponse.builder()
                .token(token)
                .user_id(vendor.getUserId())
                .vendorId(vendor.getId())
                .role(vendor.getRole())
                .cartId(vendor.getCart().getId())
                .userStatus(vendor.getUserStatus())
                .build();
    }
}
