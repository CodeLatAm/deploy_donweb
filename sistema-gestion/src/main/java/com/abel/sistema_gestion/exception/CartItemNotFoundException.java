package com.abel.sistema_gestion.exception;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(String message){
        super(message);
    }
}
