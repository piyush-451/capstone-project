package com.Capstone.Ecommerce.Exceptions;

public class ProductCurrentlyUnavailableException extends RuntimeException{
    public ProductCurrentlyUnavailableException(String message){
        super(message);
    }
}
