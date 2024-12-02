package com.abel.sistema_gestion.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message){
        super(message);
    }
}
