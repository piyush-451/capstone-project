package com.Capstone.Ecommerce.Exceptions.Handler;

import com.Capstone.Ecommerce.Exceptions.EmailNotUniqueDBException;
import com.Capstone.Ecommerce.Exceptions.ResourceNotFoundException;
import com.Capstone.Ecommerce.Exceptions.UserNotPresentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailNotUniqueDBException.class)
    public ResponseEntity<ExceptionResponse> handleEmailNotUniqueDBException(EmailNotUniqueDBException e) {
        ExceptionResponse response = ExceptionResponse.builder()
                .businessErrorDescription("Duplicate email found")
                .error(e.getMessage())
                .build();
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        System.out.println("method");
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Validation failed")
                        .validationErrors(e.getBindingResult().getFieldErrors().stream()
                                .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                                .collect(Collectors.toSet()))
                        .build());
    }

    @ExceptionHandler(UserNotPresentException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotPresentException(UserNotPresentException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Authentication failed")
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .businessErrorDescription("Not Found")
                        .error(e.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        System.out.println("mediatype");
        return new ResponseEntity<>("Acceptable MIME type: " + MediaType.APPLICATION_JSON_VALUE, HttpStatus.NOT_ACCEPTABLE);
    }



    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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
