package com.example.api.controllers;

import com.example.api.services.ValidationService;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity<?> databaseError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "database error";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ValidationService.UsernameValidationException.class)
    public ResponseEntity<?> usernameValidationError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Username is null or empty";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidationService.PasswordValidationException.class)
    public ResponseEntity<?> passwordValidationError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Password is null or empty";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidationService.EmailValidationException.class)
    public ResponseEntity<?> emailValidationError(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Email is null or not valid";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
