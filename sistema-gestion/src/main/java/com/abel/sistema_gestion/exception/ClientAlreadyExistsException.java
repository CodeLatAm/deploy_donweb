package com.abel.sistema_gestion.exception;

public class ClientAlreadyExistsException extends RuntimeException {

    public ClientAlreadyExistsException(String message){
        super(message);
    }
}
