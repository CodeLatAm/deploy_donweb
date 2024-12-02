package com.abel.sistema_gestion.exception;

public class ArticleAlreadyExistException extends RuntimeException{
    public ArticleAlreadyExistException(String message){
        super(message);
    }
}
