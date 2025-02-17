package com.Capstone.Ecommerce.Exceptions;

public class ResourceAlreadyPresentException extends RuntimeException{
    public ResourceAlreadyPresentException(String message){
        super(message);
    }
}
