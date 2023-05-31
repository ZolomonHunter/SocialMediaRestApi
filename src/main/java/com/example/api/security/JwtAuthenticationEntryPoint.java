package com.example.api.security;

import com.example.api.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("text/plain");
        String message;
        Throwable cause = authException.getCause();
        if (cause instanceof EntityNotFoundException || cause instanceof UserService.UserNotFoundException)
            message = "Wrong login or password";
        else if (cause instanceof AccessDeniedException || authException instanceof InsufficientAuthenticationException)
            message = "Please provide valid Jwt token";
        else
            message = "Unknown auth error";
        response.getWriter().write(message);
    }
}