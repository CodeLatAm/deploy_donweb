package com.abel.sistema_gestion.exception;

public class VendorNotFoundException extends RuntimeException{

    public VendorNotFoundException(String message){
        super(message);
    }
}
