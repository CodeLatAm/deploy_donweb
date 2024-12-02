package com.abel.sistema_gestion.exception;

public class ClientNotFoundException extends RuntimeException{

    public ClientNotFoundException (String message){
        super(message);
    }
}
