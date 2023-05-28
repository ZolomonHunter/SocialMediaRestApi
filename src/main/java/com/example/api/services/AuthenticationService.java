package com.example.api.services;

import com.example.api.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ValidationService validationService;

    public static class AccountAlreadyExistsException extends RuntimeException {} ;
    public AuthenticationResponse register(RegisterRequest request) {
        // validate request
        validationService.validateRegisterRequest(request);

        // search for duplicate username/email
        if (userService.isUserWithUsernameExists(request.getUsername()) ||
            userService.isUserWithEmailExists(request.getEmail()))
            throw new AccountAlreadyExistsException();

        // add user in db
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(UserRole.USER)
                .build();
        userService.addUser(user);

        // generate token for created user
        var jwt = jwtService.generateToken(user);

        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        // validate request
        validationService.validateLoginRequest(request);

        // authorize user in spring security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // generate token for authorized user
        var jwt = jwtService.generateToken(userService.getUser(request.getUsername()));

        return new AuthenticationResponse(jwt);
    }
}
