package com.Capstone.Ecommerce.Exceptions.Handler;

import com.Capstone.Ecommerce.Exceptions.InsufficientQuantityException;
import com.Capstone.Ecommerce.Exceptions.ProductCurrentlyUnavailableException;
import com.Capstone.Ecommerce.Exceptions.ResourceAlreadyPresentException;
import com.Capstone.Ecommerce.Exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        System.out.println("method exception class");
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Validation failed")
                        .validationErrors(e.getBindingResult().getFieldErrors().stream()
                                .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                                .collect(Collectors.toSet()))
                        .build());
    }

    @ExceptionHandler(ResourceAlreadyPresentException.class)
    public ResponseEntity<ExceptionResponse> handleResourceAlreadyPresentException(ResourceAlreadyPresentException e){
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Resource already exists")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Resource do not exists")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizationDeniedException(AuthorizationDeniedException e){
        System.out.println("Access Denied class");
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("UnAuthorised")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        System.out.println("database integrity error class");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Data integrity violation")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ProductCurrentlyUnavailableException.class)
    public ResponseEntity<ExceptionResponse> handleProductCurrentlyUnavailableException(ProductCurrentlyUnavailableException e){
        System.out.println("available quantity < 0");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Insufficient Product quantity")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<ExceptionResponse> handleInsufficientQuantityException(InsufficientQuantityException e){
        System.out.println("available quantity is less");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Insufficient Product quantity")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException e){
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Authentication Failed")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        System.out.println(e.getClass().getName());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Internal server error")
                        .error(e.getMessage())
                        .build());
    }


}
