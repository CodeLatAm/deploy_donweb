package com.abel.sistema_gestion.controller;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.auth.*;
import com.abel.sistema_gestion.serviceimpl.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(value = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) throws MessagingException, IOException {
        return ResponseEntity.ok( authService.register(request));
    }
    @PostMapping(value = "/login/propietario")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/login/vendedor")
    public ResponseEntity<AuthResponse> loginVendor(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.loginVendor(request));
    }

    @GetMapping("")
    public ResponseEntity<MessageResponse> verifyAccount(@RequestParam("token") String token) {
        MessageResponse response = authService.verifyCationToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-email")
    public ResponseEntity<MessageResponse> resendVerificationEmail (@Valid @RequestBody
                                                                    ResendVerificationEmailRequest request) throws MessagingException, IOException {
        return ResponseEntity.ok(authService.resendVerificationEmail(request.getUsername()));
    }


}
