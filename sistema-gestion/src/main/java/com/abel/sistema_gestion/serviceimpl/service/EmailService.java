package com.abel.sistema_gestion.serviceimpl.service;

import com.abel.sistema_gestion.entity.User;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
    void sendVerificationEmail(User user, String token) throws MessagingException, IOException;
}
