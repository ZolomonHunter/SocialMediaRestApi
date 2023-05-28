package com.example.api.controllers;

import com.example.api.models.AuthenticationResponse;
import com.example.api.models.LoginRequest;
import com.example.api.models.RegisterRequest;
import com.example.api.services.AuthenticationService;
import com.example.api.services.ValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final ValidationService validationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        validationService.validateRegisterRequest(request);
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @RequestBody LoginRequest request
    ) {
        validationService.validateLoginRequest(request);
        return ResponseEntity.ok(authService.authenticate(request));
    }


}
