package com.abel.sistema_gestion.serviceimpl;

import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.entity.VerificationToken;
import com.abel.sistema_gestion.repository.VerificationTokenRepository;
import com.abel.sistema_gestion.serviceimpl.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    @Override
    public void createVerificationToken (String token, User user) {
        // Eliminar todos los tokens existentes para el usuario
        verificationTokenRepository.deleteAllByUserId(user.getId());

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setCreatedAt(LocalDateTime.now());
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}
