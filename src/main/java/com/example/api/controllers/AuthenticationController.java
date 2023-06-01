package com.example.api.controllers;

import com.example.api.models.AuthenticationResponse;
import com.example.api.models.LoginRequest;
import com.example.api.models.PostResponse;
import com.example.api.models.RegisterRequest;
import com.example.api.services.AuthenticationService;
import com.example.api.services.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "User registration")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns jwt token for registered user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Login/password/email validation error"),
            @ApiResponse(responseCode = "409", description = "Account with the same login/email already exists")
    })
    public ResponseEntity<AuthenticationResponse> register(
            String login, String password, String email
    ) {
        return ResponseEntity.ok(authService.register(new RegisterRequest(login, password, email)));
    }


    @PostMapping("login")
    @Operation(summary = "User authentication")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns jwt token for authenticated user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Login/password validation error or wrong login/password")
    })
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
