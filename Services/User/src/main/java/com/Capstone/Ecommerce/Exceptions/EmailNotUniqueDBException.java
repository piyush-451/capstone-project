package com.Capstone.Ecommerce.Exceptions;

public class EmailNotUniqueDBException extends RuntimeException {
    public EmailNotUniqueDBException(String message) {
        super(message);
    }
}
