package com.abel.sistema_gestion.exception;

public class VerifycationTokenNotFoundException extends RuntimeException {

    public VerifycationTokenNotFoundException (String message) {
        super(message);
    }
}
