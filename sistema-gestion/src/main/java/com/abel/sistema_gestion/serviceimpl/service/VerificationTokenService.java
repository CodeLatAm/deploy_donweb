package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.entity.User;
import com.abel.sistema_gestion.entity.VerificationToken;

public interface VerificationTokenService {
    void createVerificationToken(String randomToken, User userExistInBd);

    VerificationToken findByToken(String token);
}
