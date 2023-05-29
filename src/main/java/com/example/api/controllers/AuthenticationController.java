package com.example.api.controllers;

import com.example.api.models.AuthenticationResponse;
import com.example.api.models.LoginRequest;
import com.example.api.models.RegisterRequest;
import com.example.api.services.AuthenticationService;
import com.example.api.services.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
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
