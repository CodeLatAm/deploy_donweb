package com.abel.sistema_gestion.exception;

public class VendorAlreadyExistsException extends RuntimeException{
    public VendorAlreadyExistsException(String message){
        super(message);
    }
}
