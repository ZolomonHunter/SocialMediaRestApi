package com.example.api.services;

import com.example.api.models.LoginRequest;
import com.example.api.models.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValidationService {
    public static class UsernameValidationException extends RuntimeException { }
    public static class EmailValidationException extends RuntimeException { }
    public static class PasswordValidationException extends RuntimeException { }

    public void validateRegisterRequest(RegisterRequest request) {
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());
        validateEmail(request.getEmail());
    }

    public void validateLoginRequest(LoginRequest request) {
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());
    }

    public void validateUsername(String username) {
        if (username == null || username.isBlank())
            throw new UsernameValidationException();
    }

    public void validateEmail(String email) {
        if (email == null || !Pattern.compile("^(.+)@(\\S+)$").matcher(email).matches())
            throw new EmailValidationException();
    }

    public void validatePassword(String password) {
        if (password == null || password.isBlank())
            throw new PasswordValidationException();
    }

}
