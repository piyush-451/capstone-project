package com.Capstone.Ecommerce.Exceptions;

public class UserNotPresentException extends RuntimeException {
    public UserNotPresentException(String message) {
        super(message);
    }
}
