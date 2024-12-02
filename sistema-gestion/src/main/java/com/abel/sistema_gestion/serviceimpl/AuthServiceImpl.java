package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.dto.MessageResponse;
import com.abel.sistema_gestion.dto.auth.AuthRequest;
import com.abel.sistema_gestion.dto.auth.AuthResponse;
import com.abel.sistema_gestion.dto.auth.RegisterRequest;
import com.abel.sistema_gestion.dto.auth.RegisterResponse;
import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.entity.Vendor;
import com.abel.sistema_gestion.entity.VerificationToken;
import com.abel.sistema_gestion.enums.VendorStatus;
import com.abel.sistema_gestion.exception.UserNotFoundException;
import com.abel.sistema_gestion.exception.VendorNotFoundException;
import com.abel.sistema_gestion.exception.VendorStatusException;
import com.abel.sistema_gestion.exception.VerifycationTokenNotFoundException;
import com.abel.sistema_gestion.mapper.AuthMapper;
import com.abel.sistema_gestion.repository.UserRepository;
import com.abel.sistema_gestion.repository.VendorRepository;
import com.abel.sistema_gestion.security.jwt.JwtService;
import com.abel.sistema_gestion.serviceimpl.service.AuthService;
import com.abel.sistema_gestion.serviceimpl.service.EmailService;
import com.abel.sistema_gestion.serviceimpl.service.VerificationTokenService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final VendorRepository vendorRepository;
    private final VerificationTokenService tokenService;
    public AuthServiceImpl(UserRepository userRepository, AuthMapper authMapper,
                           JwtService jwtService, EmailService emailService, AuthenticationManager
                                   authenticationManager, VendorRepository vendorRepository,
                           VerificationTokenService tokenService) {
        this.userRepository = userRepository;
        this.authMapper = authMapper;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.vendorRepository = vendorRepository;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) throws MessagingException, IOException {
        // Verificar si ya existe un usuario con el mismo nombre de usuario
        boolean existUser = userRepository.existsByUsernameAndVerificationToken(request.getUsername(), true);
        boolean userExistsWithoutValidation = userRepository.existsByUsernameAndVerificationToken(request.getUsername(), false);
        if(existUser){
            logger.error("El email ya esta en uso {}", request.getUsername());
            throw new UserNotFoundException("El email ya esta en uso " + request.getUsername());

        }
        if(userExistsWithoutValidation) {
            logger.error("Debes activar tu cuenta. Revisa tu correo electrónico o solicita un nuevo mensaje de validación.");
            throw new UserNotFoundException("Debes activar tu cuenta. Revisa tu correo electrónico o solicita un nuevo mensaje de validación.");
        }

        String randomToken = UUID.randomUUID().toString();

        User user = authMapper.mapToRegisterRequest(request);

        userRepository.save(user);
        tokenService.createVerificationToken(randomToken, user);
        String token = jwtService.getToken(user);
        emailService.sendVerificationEmail(user, randomToken);
        return authMapper.mapToRegisterResponse(token);
    }

    @Override
    public MessageResponse resendVerificationEmail (String email) throws MessagingException, IOException {
        User user = userRepository.findByUsername(email).orElseThrow(() ->
                new UserNotFoundException("Usuario no encontrado."));
        boolean existUser = userRepository.existsByUsernameAndVerificationToken(email, true);
        if(existUser) {
            throw new UserNotFoundException("El usuario con email: " + email + " ya esta validado.");
        }
        String randomToken = UUID.randomUUID().toString();
        tokenService.createVerificationToken(randomToken, user);
        emailService.sendVerificationEmail(user, randomToken);
        return MessageResponse.builder()
                .message("El correo de verificación de la cuenta ha sido reenviado. Tiene una validez de 24 horas.")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        if(!userRepository.existsByUsername(request.getUsername())){
            throw new UserNotFoundException("El Propietario no existe con email: " + request.getUsername());
        }
        if(!isUserActivated(request.getUsername())){
            throw new UserNotFoundException("Debes activar la cuenta para ingresar.");
        }

        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new UserNotFoundException("Usuario no encontrado: " + request.getUsername())
        );
        String token = jwtService.getToken(userDetails);
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new UserNotFoundException("Usuario no encontrado: " + request.getUsername())
        );

        return authMapper.mapToAuthResponse(token, user);
    }

    public boolean isUserActivated(String username) {
        Optional<User> user = userRepository.findByUsernameAndVerificationToken(username, true);
        return user.isPresent();
    }

    @Override
    public AuthResponse loginVendor(AuthRequest request) {
        if(!vendorRepository.existsByEmail(request.getUsername())){
            throw new VendorNotFoundException("El vendodor no existe con email: " + request.getUsername());
        }
        // Verificar si el vendedor tiene un estado INACTIVE o SUSPENDED
        if (vendorRepository.existsByEmailAndVendorStatusIn(
                request.getUsername(),
                List.of(VendorStatus.INACTIVE, VendorStatus.SUSPENDED))) {
            throw new VendorStatusException("El vendedor tiene un estado inactivo o suspendido y no puede ingresar");
        }
        this.authenticate(request.getUsername(), request.getPassword());
        Vendor vendor = vendorRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Vendor not found: " + request.getUsername()));
        String token = jwtService.getToken(vendor);
        return authMapper.mapToAuthResponseVendor(token, vendor);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Override
    public MessageResponse verifyCationToken(String token) {

        VerificationToken verificationToken = tokenService.findByToken(token);
        if(verificationToken == null ) {

            throw new VerifycationTokenNotFoundException("Token invalido");
        }
        if(isTokenExpired(verificationToken)) {

            throw new VerifycationTokenNotFoundException("El token ha expirado. Vuelve a enviar el correo de validación");
        }
        // Marcar al usuario como verificado
        User user = verificationToken.getUser();
        user.setVerificationToken(true);
        userRepository.save(user);
        return MessageResponse.builder()
                .message("Cuenta verificada con éxito, inicia session.")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    private boolean isTokenExpired(VerificationToken token) {
        return token.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now());
    }


    /*
    @GetMapping("/auth/verify")
public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
    VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

    if (verificationToken == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
    }

    // Verificar si el token está expirado
    if (isTokenExpired(verificationToken)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expirado");
    }

    // Marcar al usuario como verificado
    User user = verificationToken.getUser();
    user.setVerified(true);
    userRepository.save(user);

    return ResponseEntity.ok("Cuenta verificada con éxito");
}

private boolean isTokenExpired(VerificationToken token) {
    return token.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now());
}


 */
}
