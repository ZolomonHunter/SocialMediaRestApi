package com.example.api.controllers;

import com.example.api.models.AuthenticationResponse;
import com.example.api.models.LoginRequest;
import com.example.api.models.RegisterRequest;
import com.example.api.services.AuthenticationService;
import com.example.api.services.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(
            String login, String password, String email
    ) {
        return ResponseEntity.ok(authService.register(new RegisterRequest(login, password, email)));
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login (
            String login, String password
    ) {
        return ResponseEntity.ok(authService.authenticate(new LoginRequest(login, password)));
    }

    @ExceptionHandler(AuthenticationService.AccountAlreadyExistsException.class)
    public ResponseEntity<?> accountAlreadyExists() {
        return new ResponseEntity<>("User with the same login or email already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationService.EmailValidationException.class)
    public ResponseEntity<?> emailValidationError() {
        return new ResponseEntity<>("Email is null or not valid", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationService.UsernameValidationException.class)
    public ResponseEntity<?> usernameValidationError() {
        return new ResponseEntity<>("Username is null or empty", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationService.PasswordValidationException.class)
    public ResponseEntity<?> passwordValidationError() {
        return new ResponseEntity<>("Password is null or empty", HttpStatus.BAD_REQUEST);
    }
}
