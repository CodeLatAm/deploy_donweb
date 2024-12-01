package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.auth.AuthRequest;
import com.abel.sistema_gestion.dto.auth.AuthResponse;
import com.abel.sistema_gestion.dto.auth.RegisterRequest;
import com.abel.sistema_gestion.dto.auth.RegisterResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotBlank;

import java.io.IOException;

public interface AuthService {
    RegisterResponse register(RegisterRequest request) throws MessagingException, IOException;

    AuthResponse login(AuthRequest request);

    AuthResponse loginVendor(AuthRequest request);

    MessageResponse verifyCationToken(String token);

    MessageResponse resendVerificationEmail(@NotBlank(message = "Email es requerido") String username) throws MessagingException, IOException;
}
