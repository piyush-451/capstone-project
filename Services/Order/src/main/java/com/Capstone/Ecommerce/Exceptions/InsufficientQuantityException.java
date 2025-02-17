package com.Capstone.Ecommerce.Exceptions;

public class InsufficientQuantityException extends RuntimeException{
    public InsufficientQuantityException(String message){
        super(message);
    }
}
